package com.ecinemax.dto;

import com.ecinemax.entity.SeatStatus;

public class SeatDto {

    private Long id;
    private String rowLabel;
    private Integer seatNumber;
    private SeatStatus status;

    public SeatDto() {
    }

    public SeatDto(Long id, String rowLabel, Integer seatNumber, SeatStatus status) {
        this.id = id;
        this.rowLabel = rowLabel;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getRowLabel() {
        return rowLabel;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public SeatStatus getStatus() {
        return status;
    }
}
