package com.ecinemax.entity;

import java.math.BigDecimal;

// Fixed ticket categories and prices, matching the original buytickets.html.
// A plain enum rather than a database table - ticket pricing isn't
// admin-editable in this project, so a lookup table would be unused
// complexity. If that changes later, this can become an entity then.
public enum TicketType {

    SENIOR(new BigDecimal("10.00")),
    ADULT(new BigDecimal("12.00")),
    CHILD(new BigDecimal("7.00"));

    private final BigDecimal price;

    TicketType(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
