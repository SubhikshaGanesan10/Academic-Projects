package com.ecinemax.dto;

import com.ecinemax.entity.TicketType;

import java.math.BigDecimal;

public class BookingItemDto {

    private TicketType ticketType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    public BookingItemDto(TicketType ticketType, Integer quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        this.ticketType = ticketType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}
