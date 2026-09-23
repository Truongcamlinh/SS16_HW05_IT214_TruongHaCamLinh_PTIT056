package com.tiki.flashsale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlashSaleApplication {
    public static void main(String[] args) { SpringApplication.run(FlashSaleApplication.class, args); }
}
