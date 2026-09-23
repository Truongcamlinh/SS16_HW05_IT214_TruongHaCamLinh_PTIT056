package com.tiki.flashsale.warmup;

import com.tiki.flashsale.entity.FlashSaleProduct;
import com.tiki.flashsale.repository.FlashSaleProductRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component @Order(1)
public class DataInitializer implements CommandLineRunner {
    private final FlashSaleProductRepository repository;
    public DataInitializer(FlashSaleProductRepository repository) { this.repository = repository; }
    @Override public void run(String... args) {
        saveIfMissing("FS-001", "iPhone 15 Flash Sale", "15990000", 500);
        saveIfMissing("FS-002", "AirPods Pro Flash Sale", "3990000", 1000);
        saveIfMissing("FS-003", "MacBook Air Flash Sale", "21990000", 200);
    }
    private void saveIfMissing(String id, String name, String price, int quantity) {
        if (!repository.existsById(id)) repository.save(
                new FlashSaleProduct(id, name, new BigDecimal(price), quantity, true));
    }
}
