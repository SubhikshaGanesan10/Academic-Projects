package com.ecinemax.dto;

import com.ecinemax.entity.TicketType;

import java.math.BigDecimal;

public class TicketTypeDto {

    private TicketType name;
    private BigDecimal price;

    public TicketTypeDto(TicketType name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public TicketType getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
