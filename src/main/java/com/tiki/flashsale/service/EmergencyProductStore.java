package com.tiki.flashsale.service;

import com.tiki.flashsale.dto.FlashSaleProductDTO;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class EmergencyProductStore {
    private final ConcurrentHashMap<String, FlashSaleProductDTO> snapshots = new ConcurrentHashMap<>();
    public void remember(FlashSaleProductDTO product) { snapshots.put(product.productId(), product); }
    public Optional<FlashSaleProductDTO> find(String productId) {
        return Optional.ofNullable(snapshots.get(productId)).map(FlashSaleProductDTO::asDegraded);
    }
}
