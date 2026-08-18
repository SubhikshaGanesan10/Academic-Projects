package com.ecinemax.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// The live availability of one physical Seat for one specific Showtime. A
// row exists here for every seat in the screen the moment a showtime is
// created (all AVAILABLE) - booking a seat means updating this row to
// BOOKED and pointing it at the Booking, not creating a new row.
@Entity
@Table(name = "showtime_seats", uniqueConstraints = @UniqueConstraint(columnNames = {"showtime_id", "seat_id"}))
public class ShowtimeSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public ShowtimeSeat() {
    }

    public ShowtimeSeat(Showtime showtime, Seat seat, SeatStatus status) {
        this.showtime = showtime;
        this.seat = seat;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public Seat getSeat() {
        return seat;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
