# E-CinemaX

An online movie ticket booking system, rebuilt from a static HTML/CSS/JS prototype into a
full-stack Java application. This is a learning/portfolio project — the code favors clarity
and standard Spring Boot patterns over advanced abstractions.

## Tech Stack

- **Backend:** Java 17, Spring Boot 4.1.0, Spring MVC / REST
- **Data:** Spring Data JPA, Hibernate, MySQL
- **Security:** Spring Security with server-side session authentication, BCrypt password hashing
- **Frontend:** Plain HTML, CSS, and vanilla JavaScript (no frontend framework) served as static
  resources by Spring Boot, talking to the backend via REST (`fetch()`)
- **Build tool:** Maven (via the included Maven Wrapper — no local Maven install required)
- **Version control:** Git/GitHub

## Architecture

The frontend is plain static HTML/CSS/JS, served directly by Spring Boot and talking to it purely
over REST (`fetch()`) - no server-side templating, no JS framework. A request flows through a
standard layered backend:

```
Browser (HTML/CSS/JS)
   |  fetch() - JSON over HTTP, session cookie for auth
   v
Controller    <- thin HTTP layer: reads the request, calls a service, returns a DTO
   v
Service       <- business logic (e.g. "can this seat be booked?"), @Transactional boundaries
   v
Repository    <- Spring Data JPA interfaces; no implementation code, Spring generates the SQL
   v
Entity / MySQL
```

DTOs (`dto/`) keep the JSON shape sent to the browser separate from the JPA entities/database
schema - so, for example, `AppUser.passwordHash` can never accidentally end up in an API response.

## Project Status

Built incrementally, phase by phase. Current status: **all 8 phases complete.**

| Phase | Description | Status |
|---|---|---|
| 0 | Project setup (this scaffold) | Done |
| 1 | Database/data layer | Done |
| 2 | Movie browsing and movie details | Done |
| 3 | Authentication | Done |
| 4 | Booking and seat selection | Done |
| 5 | Customer account and booking history | Done |
| 6 | Admin functionality | Done |
| 7 | Testing, validation, error handling, cleanup | Done |
| 8 | Documentation polish | Done |

## Running the App

First-time setup: copy `src/main/resources/application.properties.example` to
`application.properties` (gitignored, since it holds real secrets) and fill in your MySQL
credentials plus an admin email/password of your own choosing (`app.admin.seed-email` /
`app.admin.seed-password`).

No local Maven install is needed — the Maven Wrapper (`mvnw`) downloads the exact Maven version
the project expects.

```
# Windows
mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

Then open http://localhost:8080/Cinema.html in a browser.

Requires a local MySQL server running on port 3306, with a database `ecinemax_db` and a user
`ecinemax_app` matching the credentials in `application.properties`. On first startup, Flyway
creates all tables from `src/main/resources/db/migration/V1__init.sql`, and the app seeds 15
sample movies plus an admin account using **the email/password you set** in
`app.admin.seed-email` / `app.admin.seed-password` (nothing is hardcoded — leave them blank and
no admin account gets created at all). That admin lands on `AdminMainPage.html`, with
movie/showtime/promotion CRUD, user management (enable/disable), and a read-only view of every
booking across all customers.

- **Customer accounts:** created via the Registration page (`Registration.html`)

Login uses server-side sessions (a cookie), not tokens - once logged in via `Customerlogin.html`,
the browser stays authenticated across requests until logout or the session expires.

Seed data includes one screen (6 rows x 8 seats) and showtimes for every Now Showing movie over
the next 3 days, so there's something real to book. Payment is entirely mocked - no real payment
gateway is contacted, and only the last 4 digits of any card number entered are ever stored.

## API Overview

All endpoints return/accept JSON. `/api/auth/**` and `/api/movies/**` are public; everything else
under `/api/**` requires login, and `/api/admin/**` requires the ADMIN role (enforced in
`SecurityConfig`, not repeated per-controller).

| Area | Method + Path | Purpose |
|---|---|---|
| Auth | `POST /api/auth/register` | Create a customer account |
| | `POST /api/auth/login` | Log in (starts a session) |
| | `POST /api/auth/logout` | Log out |
| | `GET /api/auth/me` | Who am I / am I logged in |
| Movies | `GET /api/movies`, `GET /api/movies?status=` | Browse movies |
| | `GET /api/movies/{id}` | Movie detail |
| | `GET /api/movies/{id}/showtimes` | Showtimes for a movie |
| Booking | `GET /api/ticket-types` | Ticket types + prices |
| | `GET /api/showtimes/{id}/seats` | Seat map for a showtime |
| | `POST /api/bookings` | Create a booking (pending payment) |
| | `POST /api/bookings/{id}/payment` | Submit mock payment |
| | `GET /api/bookings/me`, `GET /api/bookings/{id}` | Booking history / detail |
| Account | `GET/PUT /api/users/me` | View/edit profile |
| | `PUT /api/users/me/password` | Change password |
| Admin | `POST/PUT/DELETE /api/admin/movies` | Movie CRUD |
| | `GET /api/admin/screens`, `POST /api/admin/showtimes` | Schedule showtimes |
| | `GET/POST/DELETE /api/admin/promotions` | Promotion CRUD |
| | `GET /api/admin/users`, `PUT /api/admin/users/{id}/status` | User management |
| | `GET /api/admin/bookings` | All bookings (oversight) |

## Known Gaps / Backlog

Deliberately deferred, not forgotten:

- **Forgot/reset password** — the link on `Customerlogin.html` is still a placeholder. Would need
  a reset-token flow and either a real or mocked email step; not core to the booking flow, so
  left out to keep the auth scope simple.
- **Promo codes aren't applied at checkout yet** — Promotion CRUD exists for admins
  (`promotions.html`), but wiring `checkout.html` to actually validate and apply a promo code to
  a booking's total was left out - it touches booking-total calculation, which was kept separate
  from admin CRUD on purpose.

Resolved in Phase 7 (kept here briefly for context, since they were open when Phase 6 shipped):
seat-booking concurrency now uses optimistic locking (`ShowtimeSeat.version`) instead of a plain
check-then-act race, and abandoned `PENDING` bookings are auto-cancelled and their seats released
by a scheduled job (`PendingBookingCleanupTask`, every 5 minutes, 15-minute expiry).

## Project Structure

```
src/main/java/com/ecinemax/
    entity/       JPA entities (Movie, Showtime, ...) - map directly to database tables
    repository/   Spring Data JPA repositories - data access, no implementation code needed
    service/      Business logic, converts entities to DTOs
    controller/   REST controllers - thin HTTP layer, delegates to services
    dto/          Request/response shapes sent to the browser as JSON
    config/       App configuration and startup logic (e.g. DataSeeder)
    security/     Spring Security config and the UserDetailsService bridge to AppUser
    exception/    GlobalExceptionHandler - turns validation/service errors into clean JSON
src/main/resources/db/migration/   Flyway migration scripts (V1__init.sql, ...) - the schema
src/main/resources/static/    The existing HTML/CSS/JS frontend, served directly by Spring Boot
src/main/resources/application.properties   App configuration (datasource, JPA settings)
pom.xml                       Maven build file and dependency list
```
