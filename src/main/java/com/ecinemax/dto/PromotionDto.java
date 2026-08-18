package com.ecinemax.dto;

public class PromotionDto {

    private Long id;
    private String name;
    private String code;
    private Integer discountPercent;

    public PromotionDto(Long id, String name, String code, Integer discountPercent) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.discountPercent = discountPercent;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }
}
