package com.ecinemax.dto;

import com.ecinemax.entity.BookingStatus;
import com.ecinemax.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// Used by both checkout.html (before payment) and Orderconfirmation.html
// (after payment) - the only difference is whether the payment fields are
// filled in yet.
public class BookingDto {

    private Long id;
    private String bookingReference;
    private BookingStatus status;

    private String movieTitle;
    private LocalDate showDate;
    private LocalTime showTime;
    private List<String> seatLabels;

    private List<BookingItemDto> items;
    private BigDecimal subtotal;
    private BigDecimal total;

    private PaymentMethod paymentMethod;
    private String maskedCardLast4;
    private String transactionRef;
    private LocalDateTime paidAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public LocalTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalTime showTime) {
        this.showTime = showTime;
    }

    public List<String> getSeatLabels() {
        return seatLabels;
    }

    public void setSeatLabels(List<String> seatLabels) {
        this.seatLabels = seatLabels;
    }

    public List<BookingItemDto> getItems() {
        return items;
    }

    public void setItems(List<BookingItemDto> items) {
        this.items = items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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
