package com.ecinemax.controller;

import com.ecinemax.dto.CreateShowtimeRequest;
import com.ecinemax.dto.ScreenDto;
import com.ecinemax.dto.ShowtimeDto;
import com.ecinemax.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminShowtimeController {

    private final BookingService bookingService;

    public AdminShowtimeController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/screens")
    public List<ScreenDto> getScreens() {
        return bookingService.getScreens();
    }

    @PostMapping("/showtimes")
    public ShowtimeDto createShowtime(@Valid @RequestBody CreateShowtimeRequest request) {
        return bookingService.createShowtime(request);
    }
}
