package com.ecinemax.dto;

import com.ecinemax.entity.TicketType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TicketItemRequest {

    @NotNull(message = "Ticket type is required")
    private TicketType ticketType;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
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
