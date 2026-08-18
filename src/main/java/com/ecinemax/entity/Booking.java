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

import java.math.BigDecimal;
import java.time.LocalDateTime;

// One booking transaction: a user reserving seats for one showtime.
// Payment is mocked, so the payment outcome is stored directly here rather
// than in a separate Payment table/gateway integration - there is no real
// payment gateway in this project, so a separate aggregate would add
// structure without adding real behavior.
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    private String bookingReference;
    private LocalDateTime bookingDateTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private BigDecimal subtotal;
    private BigDecimal total;

    // Set once payment is submitted (POST /api/bookings/{id}/payment).
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String maskedCardLast4;
    private String transactionRef;
    private LocalDateTime paidAt;

    public Booking() {
    }

    public Booking(AppUser user, Showtime showtime, String bookingReference, BigDecimal subtotal, BigDecimal total) {
        this.user = user;
        this.showtime = showtime;
        this.bookingReference = bookingReference;
        this.bookingDateTime = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
        this.subtotal = subtotal;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getMaskedCardLast4() {
        return maskedCardLast4;
    }

    public void setMaskedCardLast4(String maskedCardLast4) {
        this.maskedCardLast4 = maskedCardLast4;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
