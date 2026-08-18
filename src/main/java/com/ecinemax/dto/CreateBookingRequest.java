package com.ecinemax.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateBookingRequest {

    @NotNull(message = "showtimeId is required")
    private Long showtimeId;

    @NotEmpty(message = "Select at least one seat")
    private List<Long> seatIds;

    @NotEmpty(message = "Select at least one ticket")
    @Valid
    private List<TicketItemRequest> ticketItems;

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public List<TicketItemRequest> getTicketItems() {
        return ticketItems;
    }

    public void setTicketItems(List<TicketItemRequest> ticketItems) {
        this.ticketItems = ticketItems;
    }
}
