package com.ecinemax.controller;

import com.ecinemax.dto.BookingDto;
import com.ecinemax.dto.CreateBookingRequest;
import com.ecinemax.dto.PaymentRequest;
import com.ecinemax.dto.SeatMapDto;
import com.ecinemax.dto.TicketTypeDto;
import com.ecinemax.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/api/ticket-types")
    public List<TicketTypeDto> getTicketTypes() {
        return bookingService.getTicketTypes();
    }

    @GetMapping("/api/showtimes/{id}/seats")
    public SeatMapDto getSeatMap(@PathVariable Long id) {
        return bookingService.getSeatMap(id);
    }

    @PostMapping("/api/bookings")
    public BookingDto createBooking(@Valid @RequestBody CreateBookingRequest request, Authentication authentication) {
        return bookingService.createBooking(request, authentication.getName());
    }

    @PostMapping("/api/bookings/{id}/payment")
    public BookingDto submitPayment(@PathVariable Long id, @Valid @RequestBody PaymentRequest request, Authentication authentication) {
        return bookingService.submitPayment(id, request, authentication.getName());
    }

    @GetMapping("/api/bookings/me")
    public List<BookingDto> getMyBookings(Authentication authentication) {
        return bookingService.getBookingsForUser(authentication.getName());
    }

    @GetMapping("/api/bookings/{id}")
    public BookingDto getBooking(@PathVariable Long id, Authentication authentication) {
        return bookingService.getBooking(id, authentication.getName());
    }
}
