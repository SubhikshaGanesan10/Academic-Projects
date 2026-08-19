package com.ecinemax.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PromotionRequest {

    @NotBlank(message = "Promotion name is required")
    private String name;

    @NotBlank(message = "Promo code is required")
    private String code;

    @NotNull(message = "Discount is required")
    @Min(value = 1, message = "Discount must be between 1 and 100")
    @Max(value = 100, message = "Discount must be between 1 and 100")
    private Integer discountPercent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }
}
