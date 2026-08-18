package com.ecinemax.dto;

import com.ecinemax.entity.TicketType;

public class TicketItemRequest {

    private TicketType ticketType;
    private Integer quantity;

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
