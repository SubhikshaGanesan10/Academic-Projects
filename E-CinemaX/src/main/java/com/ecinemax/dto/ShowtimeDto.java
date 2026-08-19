package com.ecinemax.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShowtimeDto {

    private Long id;
    private LocalDate showDate;
    private LocalTime showTime;

    public ShowtimeDto() {
    }

    public ShowtimeDto(Long id, LocalDate showDate, LocalTime showTime) {
        this.id = id;
        this.showDate = showDate;
        this.showTime = showTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
