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

## Project Status

Built incrementally, phase by phase. Current status: **Phase 6 — admin functionality complete.**

| Phase | Description | Status |
|---|---|---|
| 0 | Project setup (this scaffold) | Done |
| 1 | Database/data layer | Done |
| 2 | Movie browsing and movie details | Done |
| 3 | Authentication | Done |
| 4 | Booking and seat selection | Done |
| 5 | Customer account and booking history | Done |
| 6 | Admin functionality | Done |
| 7 | Testing, validation, error handling, cleanup | Not started |
| 8 | Documentation polish | Not started |

## Running the App

First-time setup: copy `src/main/resources/application.properties.example` to
`application.properties` (gitignored, since it holds a real DB password) and fill in your
MySQL credentials.

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
`ecinemax_app` matching the credentials in `application.properties`. On first startup, the
database tables are created automatically and seeded with 15 sample movies plus a seeded admin
account:

- **Admin login:** `admin@ecinemax.com` / `Admin123!` — lands on `AdminMainPage.html`, with movie/showtime/promotion CRUD, user management (enable/disable), and a read-only view of every booking across all customers.
- **Customer accounts:** created via the Registration page (`Registration.html`)

Login uses server-side sessions (a cookie), not tokens - once logged in via `Customerlogin.html`,
the browser stays authenticated across requests until logout or the session expires.

Seed data includes one screen (6 rows x 8 seats) and showtimes for every Now Showing movie over
the next 3 days, so there's something real to book. Payment is entirely mocked - no real payment
gateway is contacted, and only the last 4 digits of any card number entered are ever stored.

## Known Gaps / Backlog

Deliberately deferred, not forgotten:

- **Forgot/reset password** — the link on `Customerlogin.html` is still a placeholder. Needs a
  reset-token flow and either a real or mocked email step; not core to the booking flow, so
  postponed until after the main phases are done (Phase 7 or its own small phase).
- **Promo codes aren't applied at checkout yet** — Phase 6 built real Promotion CRUD for admins
  (`promotions.html`), but wiring `checkout.html` to actually validate and apply a promo code to
  a booking's total is deferred to Phase 7 - it touches booking-total calculation, which was
  intentionally kept out of Phase 6's scope to keep that phase to admin CRUD only.
- **Seat booking concurrency** — two people selecting the same seat at the exact same moment is
  checked (the second one gets a 409), but there's no optimistic locking, so it's a "check then
  act" race rather than a fully atomic guarantee. Planned hardening for Phase 7.
- **Abandoned checkouts don't release seats** — if someone reaches `checkout.html` (creating a
  PENDING booking, which marks seats BOOKED) and never pays, those seats stay reserved
  indefinitely. A real system would expire PENDING bookings after a few minutes; deferred to
  Phase 7.

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
src/main/resources/static/    The existing HTML/CSS/JS frontend, served directly by Spring Boot
src/main/resources/application.properties   App configuration (datasource, JPA settings)
pom.xml                       Maven build file and dependency list
```
