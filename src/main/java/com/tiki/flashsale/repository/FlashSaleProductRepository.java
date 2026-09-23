package com.tiki.flashsale.repository;

import com.tiki.flashsale.entity.FlashSaleProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashSaleProductRepository extends JpaRepository<FlashSaleProduct, String> {
    List<FlashSaleProduct> findByFlashSaleTrue();
}
