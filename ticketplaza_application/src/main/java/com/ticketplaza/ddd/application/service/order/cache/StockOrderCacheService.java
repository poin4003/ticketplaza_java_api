package com.ticketplaza.ddd.application.service.order.cache;

import com.ticketplaza.ddd.application.model.cache.TicketDetailCache;
import com.ticketplaza.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.ticketplaza.ddd.infrastructure.cache.redis.RedisInfrasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockOrderCacheService {
    private final TicketDetailCacheService ticketDetailCacheService;
    private final RedisInfrasService redisInfrasService;

    // Warm-up cache function
    public boolean AddStockAvailableToCache(Long ticketId) {
        // Check validation (*)
        if (ticketId == null) {
            return false;
        }

        // Get stock available from database
        TicketDetailCache ticketDetailCache = ticketDetailCacheService.getTicketDetail(ticketId, null);
        if (ticketDetailCache == null) {
            return false;
        }
        String keyStockItemCache = getKeyStockItemCache(ticketId);
        log.info("get->getKeyStockItemCache() | {} {} {}", ticketId, keyStockItemCache, ticketDetailCache.getTicketDetail().getStockAvailable());
        // stockAvailable = ticketDetailCache.getTicketDetail().getStockAvailable();
        redisInfrasService.setInt(keyStockItemCache, ticketDetailCache.getTicketDetail().getStockAvailable());
        return true;
    }

    // decreaseStockCache
    public int decreaseStockCache(Long ticketId, Integer quantity) {
        // 1. Get stock available
        String keyStockLua = getKeyStockItemCache(ticketId);
        String luaScript = "local stock = tonumber(redis.call('GET', KEYS[1]));" +
                "if (stock ~= nil and stock >= tonumber(ARGV[1])) then " +
                "    redis.call('SET', KEYS[1], stock - tonumber(ARGV[1]));" +
                "    return stock;" +
                "end; " +
                "return 0;";
        // Execute Lua script
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long oldStockAvailable = redisInfrasService.getRedisTemplate().execute(redisScript, Collections.singletonList(keyStockLua), quantity);

        return oldStockAvailable.intValue();
    }

    private String getKeyStockItemCache(Long ticketId) {
        return "TICKET:" + ticketId + ":STOCK";
    }
}
