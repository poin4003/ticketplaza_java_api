package com.ticketplaza.ddd.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ticketplaza.ddd.domain.model.entity.TicketDetail;
import com.ticketplaza.ddd.domain.service.TicketDetailDomainService;
import com.ticketplaza.ddd.infrastructure.cache.redis.RedisInfrasService;
import com.ticketplaza.ddd.infrastructure.distributed.redisson.RedisDistributedLocker;
import com.ticketplaza.ddd.infrastructure.distributed.redisson.RedisDistributedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketDetailCacheService {
    @Autowired
    private RedisDistributedService redisDistributedService;
    @Autowired
    private RedisInfrasService redisInfrasService;
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    // use guava
    private final static Cache<Long, TicketDetail> ticketDetailLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(8)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    private TicketDetail getTicketDetailLocalCache(Long id, Long version) {
        try {
            return ticketDetailLocalCache.getIfPresent(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TicketDetail getTicketDefaultCache(Long id, Long version) {
        // 1. Get Item local cache
        TicketDetail ticketDetail = getTicketDetailLocalCache(id, version);
        if (ticketDetail != null) {
            log.info("FROM LOCAL CACHE EXIST {}", ticketDetail);
            return ticketDetail;
        }

        // 2. Get Distributed cache
        ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
        if (ticketDetail != null) {
            log.info("FROM DISTRIBUTED CACHE EXIST {}", ticketDetail);
            ticketDetailLocalCache.put(id, ticketDetail); // Set item to local cache
            return ticketDetail;
        }

        // Cache miss
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("LOCK_KEY_ITEM" + id);
        try {
            // 1. Create lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                log.info("LOCK WAIT ITEM PLEASE... {}", version);
                return null;
            }

            // Re-check after acquiring the lock (it might have been loaded by another thread during the lock)
            ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);

            // 2. YES -> Hit cache
            if (ticketDetail != null) {
                log.info("FROM CACHE {}, {}, {}",  id, version, ticketDetail);
                ticketDetailLocalCache.put(id, ticketDetail); // Set item to local cache
                return ticketDetail;
            }

            // 3. NO -> Miss cache
            ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
            log.info("FROM DBS ->>> {}, {}", ticketDetail, version);
            if (ticketDetail == null) {
                log.info("TICKET NOT EXIST....{}", version);
                // Set cache
                redisInfrasService.setObject(genEventItemKey(id), null);
                ticketDetailLocalCache.put(id, null); // Set item to local cache
                return null;
            }

            redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
            ticketDetailLocalCache.put(id, ticketDetail);
            return ticketDetail;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    public boolean orderTicketByUser(Long ticketId) {
        ticketDetailLocalCache.invalidate(ticketId); // Remove local cache

        redisInfrasService.delete(genEventItemKey(ticketId));

        return true;
    }

    private String genEventItemKey(Long itemId) {
        return "TICKET:ITEM:" + itemId;
    }
}
