package com.tiki.flashsale.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tiki.flashsale.dto.FlashSaleProductDTO;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.cache.annotation.Cacheable;

class FlashSaleProductServiceTest {
    @Test void cacheableUsesSyncTrueToPreventLocalStampede() throws Exception {
        Cacheable annotation = FlashSaleProductService.class.getMethod("getProductById", String.class)
                .getAnnotation(Cacheable.class);
        assertThat(annotation).isNotNull();
        assertThat(annotation.sync()).isTrue();
    }

    @Test void delegatesCacheMissToRateLimitedDatabaseBean() {
        DatabaseFallbackService database = mock(DatabaseFallbackService.class);
        var expected = new FlashSaleProductDTO("FS-001", "Phone", BigDecimal.TEN, 10, false);
        when(database.loadFromDatabase("FS-001")).thenReturn(expected);
        var service = new FlashSaleProductService(database);

        assertThat(service.getProductById("FS-001")).isEqualTo(expected);
        verify(database).loadFromDatabase("FS-001");
    }

    @Test void emergencySnapshotIsMarkedAsDegraded() {
        EmergencyProductStore store = new EmergencyProductStore();
        store.remember(new FlashSaleProductDTO("FS-001", "Phone", BigDecimal.TEN, 10, false));
        assertThat(store.find("FS-001")).get().extracting(FlashSaleProductDTO::degraded).isEqualTo(true);
    }
}
