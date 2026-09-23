package com.tiki.flashsale.service;

import com.tiki.flashsale.dto.FlashSaleProductDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FlashSaleProductService {
    private final DatabaseFallbackService databaseFallback;
    public FlashSaleProductService(DatabaseFallbackService databaseFallback) {
        this.databaseFallback = databaseFallback;
    }

    @Cacheable(cacheNames = "flashSaleProducts", key = "#productId", sync = true)
    public FlashSaleProductDTO getProductById(String productId) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId không được rỗng");
        }
        return databaseFallback.loadFromDatabase(productId.trim());
    }
}
