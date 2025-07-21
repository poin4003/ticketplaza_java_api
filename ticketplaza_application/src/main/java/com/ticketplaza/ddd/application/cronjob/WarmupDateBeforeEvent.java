package com.ticketplaza.ddd.application.cronjob;

import com.ticketplaza.ddd.application.service.order.cache.StockOrderCacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WarmupDateBeforeEvent {
    private final StockOrderCacheService stockOrderCacheService;

    @PostConstruct
    public void localDataTicketItemOnce() {
        log.info("Load ticket item Once...warmup...| {}", System.currentTimeMillis());
        stockOrderCacheService.AddStockAvailableToCache(4L);
    }
}
