package com.ecinemax.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// What GET /api/showtimes/{id}/seats returns: enough context to render
// buytickets.html's header (movie/date/time) plus the full seat grid.
public class SeatMapDto {

    private Long showtimeId;
    private Long movieId;
    private String movieTitle;
    private LocalDate showDate;
    private LocalTime showTime;
    private List<SeatDto> seats;

    public SeatMapDto() {
    }

    public SeatMapDto(Long showtimeId, Long movieId, String movieTitle, LocalDate showDate, LocalTime showTime, List<SeatDto> seats) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.showDate = showDate;
        this.showTime = showTime;
        this.seats = seats;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public LocalTime getShowTime() {
        return showTime;
    }

    public List<SeatDto> getSeats() {
        return seats;
    }
}
