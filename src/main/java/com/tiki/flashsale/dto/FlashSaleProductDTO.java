package com.tiki.flashsale.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record FlashSaleProductDTO(String productId, String name, BigDecimal salePrice,
                                  int availableQuantity, boolean degraded) implements Serializable {
    public FlashSaleProductDTO asDegraded() {
        return new FlashSaleProductDTO(productId, name, salePrice, availableQuantity, true);
    }
}
