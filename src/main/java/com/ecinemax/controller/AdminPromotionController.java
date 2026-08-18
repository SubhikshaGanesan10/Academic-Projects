package com.ecinemax.controller;

import com.ecinemax.dto.PromotionDto;
import com.ecinemax.dto.PromotionRequest;
import com.ecinemax.service.PromotionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/promotions")
public class AdminPromotionController {

    private final PromotionService promotionService;

    public AdminPromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public List<PromotionDto> getPromotions() {
        return promotionService.getPromotions();
    }

    @PostMapping
    public PromotionDto createPromotion(@RequestBody PromotionRequest request) {
        return promotionService.createPromotion(request);
    }

    @DeleteMapping("/{id}")
    public void deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
    }
}
