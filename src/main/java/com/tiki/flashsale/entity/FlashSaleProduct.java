package com.tiki.flashsale.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "flash_sale_products")
public class FlashSaleProduct {
    @Id private String productId;
    private String name;
    private BigDecimal salePrice;
    private int availableQuantity;
    private boolean flashSale;

    protected FlashSaleProduct() {}
    public FlashSaleProduct(String id, String name, BigDecimal price, int quantity, boolean flashSale) {
        this.productId = id; this.name = name; this.salePrice = price;
        this.availableQuantity = quantity; this.flashSale = flashSale;
    }
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public BigDecimal getSalePrice() { return salePrice; }
    public int getAvailableQuantity() { return availableQuantity; }
    public boolean isFlashSale() { return flashSale; }
}
