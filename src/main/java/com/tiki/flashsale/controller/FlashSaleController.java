package com.tiki.flashsale.controller;

import com.tiki.flashsale.dto.FlashSaleProductDTO;
import com.tiki.flashsale.service.FlashSaleProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flash-sale/products")
public class FlashSaleController {
    private final FlashSaleProductService service;
    public FlashSaleController(FlashSaleProductService service) { this.service = service; }
    @GetMapping("/{productId}") FlashSaleProductDTO get(@PathVariable String productId) {
        return service.getProductById(productId);
    }
}
