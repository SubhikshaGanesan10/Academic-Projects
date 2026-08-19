-- Initial schema, captured from the working database after Hibernate's
-- ddl-auto=update built it incrementally across Phases 1-7. From here on,
-- schema changes are new versioned migration files (V2__..., V3__...)
-- instead of Hibernate auto-generating DDL.

CREATE TABLE app_users (
    id bigint NOT NULL AUTO_INCREMENT,
    date_of_birth date DEFAULT NULL,
    email varchar(255) NOT NULL,
    enabled bit(1) NOT NULL,
    first_name varchar(255) DEFAULT NULL,
    last_name varchar(255) DEFAULT NULL,
    password_hash varchar(255) NOT NULL,
    phone varchar(255) DEFAULT NULL,
    role enum('ADMIN','CUSTOMER') DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_app_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE movies (
    id bigint NOT NULL AUTO_INCREMENT,
    cast varchar(255) DEFAULT NULL,
    description varchar(2000) DEFAULT NULL,
    director varchar(255) DEFAULT NULL,
    duration_minutes int DEFAULT NULL,
    genre varchar(255) DEFAULT NULL,
    poster_url varchar(255) DEFAULT NULL,
    producer varchar(255) DEFAULT NULL,
    rating double DEFAULT NULL,
    release_date date DEFAULT NULL,
    status enum('COMING_SOON','NOW_SHOWING') DEFAULT NULL,
    title varchar(255) DEFAULT NULL,
    youtube_trailer_id varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE screens (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    seats_per_row int DEFAULT NULL,
    total_rows int DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE seats (
    id bigint NOT NULL AUTO_INCREMENT,
    row_label varchar(255) DEFAULT NULL,
    seat_number int DEFAULT NULL,
    screen_id bigint DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_seats_screen_row_number (screen_id, row_label, seat_number),
    CONSTRAINT fk_seats_screen FOREIGN KEY (screen_id) REFERENCES screens (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE showtimes (
    id bigint NOT NULL AUTO_INCREMENT,
    show_date date DEFAULT NULL,
    show_time time DEFAULT NULL,
    movie_id bigint DEFAULT NULL,
    screen_id bigint DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_showtimes_movie (movie_id),
    KEY idx_showtimes_screen (screen_id),
    CONSTRAINT fk_showtimes_movie FOREIGN KEY (movie_id) REFERENCES movies (id),
    CONSTRAINT fk_showtimes_screen FOREIGN KEY (screen_id) REFERENCES screens (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE bookings (
    id bigint NOT NULL AUTO_INCREMENT,
    booking_date_time datetime(6) DEFAULT NULL,
    booking_reference varchar(255) DEFAULT NULL,
    masked_card_last4 varchar(255) DEFAULT NULL,
    paid_at datetime(6) DEFAULT NULL,
    payment_method enum('CREDIT_CARD','DEBIT_CARD','PAYPAL') DEFAULT NULL,
    status enum('CANCELLED','CONFIRMED','PENDING') DEFAULT NULL,
    subtotal decimal(38,2) DEFAULT NULL,
    total decimal(38,2) DEFAULT NULL,
    transaction_ref varchar(255) DEFAULT NULL,
    showtime_id bigint DEFAULT NULL,
    user_id bigint DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_bookings_showtime (showtime_id),
    KEY idx_bookings_user (user_id),
    CONSTRAINT fk_bookings_showtime FOREIGN KEY (showtime_id) REFERENCES showtimes (id),
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES app_users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE showtime_seats (
    id bigint NOT NULL AUTO_INCREMENT,
    status enum('AVAILABLE','BOOKED') DEFAULT NULL,
    booking_id bigint DEFAULT NULL,
    seat_id bigint DEFAULT NULL,
    showtime_id bigint DEFAULT NULL,
    version int NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_showtime_seats_showtime_seat (showtime_id, seat_id),
    KEY idx_showtime_seats_booking (booking_id),
    KEY idx_showtime_seats_seat (seat_id),
    CONSTRAINT fk_showtime_seats_seat FOREIGN KEY (seat_id) REFERENCES seats (id),
    CONSTRAINT fk_showtime_seats_showtime FOREIGN KEY (showtime_id) REFERENCES showtimes (id),
    CONSTRAINT fk_showtime_seats_booking FOREIGN KEY (booking_id) REFERENCES bookings (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE booking_items (
    id bigint NOT NULL AUTO_INCREMENT,
    line_total decimal(38,2) DEFAULT NULL,
    quantity int DEFAULT NULL,
    ticket_type enum('ADULT','CHILD','SENIOR') DEFAULT NULL,
    unit_price decimal(38,2) DEFAULT NULL,
    booking_id bigint DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_booking_items_booking (booking_id),
    CONSTRAINT fk_booking_items_booking FOREIGN KEY (booking_id) REFERENCES bookings (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE promotions (
    id bigint NOT NULL AUTO_INCREMENT,
    code varchar(255) DEFAULT NULL,
    discount_percent int DEFAULT NULL,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_promotions_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
