package com.ecinemax.dto;

import java.util.List;

public class CreateBookingRequest {

    private Long showtimeId;
    private List<Long> seatIds;
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
