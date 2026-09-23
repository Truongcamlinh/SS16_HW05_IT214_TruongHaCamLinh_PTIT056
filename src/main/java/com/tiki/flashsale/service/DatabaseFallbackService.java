package com.tiki.flashsale.service;

import com.tiki.flashsale.dto.FlashSaleProductDTO;
import com.tiki.flashsale.exception.ProductUnavailableException;
import com.tiki.flashsale.repository.FlashSaleProductRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseFallbackService {
    private static final Logger log = LoggerFactory.getLogger(DatabaseFallbackService.class);
    private final FlashSaleProductRepository repository;
    private final EmergencyProductStore emergencyStore;
    public DatabaseFallbackService(FlashSaleProductRepository repository, EmergencyProductStore emergencyStore) {
        this.repository = repository;
        this.emergencyStore = emergencyStore;
    }

    @Transactional(readOnly = true)
    @RateLimiter(name = "databaseFallback", fallbackMethod = "rateLimitedFallback")
    public FlashSaleProductDTO loadFromDatabase(String productId) {
        log.info("DB fallback được cấp phép cho productId={}", productId);
        FlashSaleProductDTO dto = repository.findById(productId)
                .map(p -> new FlashSaleProductDTO(p.getProductId(), p.getName(), p.getSalePrice(),
                        p.getAvailableQuantity(), false))
                .orElseThrow(() -> new ProductUnavailableException("Không tìm thấy sản phẩm " + productId));
        emergencyStore.remember(dto);
        return dto;
    }

    FlashSaleProductDTO rateLimitedFallback(String productId, Throwable error) {
        log.warn("DB RateLimiter từ chối productId={}; thử snapshot RAM", productId);
        return emergencyStore.find(productId).orElseThrow(() -> new ProductUnavailableException(
                "Hệ thống đang quá tải, không có snapshot cho " + productId));
    }
}
