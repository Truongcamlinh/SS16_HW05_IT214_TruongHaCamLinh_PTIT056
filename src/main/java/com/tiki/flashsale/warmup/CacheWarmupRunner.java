package com.tiki.flashsale.warmup;

import com.tiki.flashsale.repository.FlashSaleProductRepository;
import com.tiki.flashsale.service.FlashSaleProductService;
import org.slf4j.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component @Order(2)
public class CacheWarmupRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CacheWarmupRunner.class);
    private final FlashSaleProductRepository repository;
    private final FlashSaleProductService service;
    public CacheWarmupRunner(FlashSaleProductRepository repository, FlashSaleProductService service) {
        this.repository = repository;
        this.service = service;
    }
    @Override public void run(String... args) {
        var products = repository.findByFlashSaleTrue();
        log.info("Bắt đầu warm-up {} sản phẩm Flash Sale", products.size());
        products.forEach(product -> {
            try {
                service.getProductById(product.getProductId());
            } catch (RuntimeException error) {
                log.warn("Warm-up thất bại cho {}: {}", product.getProductId(), error.getMessage());
            }
        });
        log.info("Hoàn tất Cache Warm-up");
    }
}
