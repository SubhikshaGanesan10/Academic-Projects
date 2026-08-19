package com.ecinemax.service;

import com.ecinemax.dto.PromotionDto;
import com.ecinemax.dto.PromotionRequest;
import com.ecinemax.entity.Promotion;
import com.ecinemax.repository.PromotionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<PromotionDto> getPromotions() {
        return promotionRepository.findAll().stream().map(this::toDto).toList();
    }

    public PromotionDto createPromotion(PromotionRequest request) {
        Promotion promotion = new Promotion(request.getName(), request.getCode(), request.getDiscountPercent());
        promotionRepository.save(promotion);
        return toDto(promotion);
    }

    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found: " + id);
        }
        promotionRepository.deleteById(id);
    }

    private PromotionDto toDto(Promotion promotion) {
        return new PromotionDto(promotion.getId(), promotion.getName(), promotion.getCode(), promotion.getDiscountPercent());
    }
}
