package com.ticketplaza.ddd.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ticketplaza.ddd.application.model.cache.TicketDetailCache;
import com.ticketplaza.ddd.domain.model.entity.TicketDetail;
import com.ticketplaza.ddd.domain.service.TicketDetailDomainService;
import com.ticketplaza.ddd.infrastructure.cache.redis.RedisInfrasService;
import com.ticketplaza.ddd.infrastructure.distributed.redisson.RedisDistributedLocker;
import com.ticketplaza.ddd.infrastructure.distributed.redisson.RedisDistributedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketDetailCacheService {
    private final RedisDistributedService redisDistributedService;
    private final RedisInfrasService redisInfrasService;
    private final TicketDetailDomainService ticketDetailDomainService;

    // use guava
    private final static Cache<Long, TicketDetailCache> ticketDetailLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(8)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public TicketDetailCache getTicketDetail(Long id, Long version) {
        // 1. Get Item local cache
        TicketDetailCache ticketDetailCache = getTicketDetailLocalCache(id);

        if (ticketDetailCache != null) {
            if (version == null || version.compareTo(ticketDetailCache.getVersion()) <= 0) {
                log.info("GET TICKET FROM LOCAL CACHE WITH VersionUser: {} AND Version {}", version, ticketDetailCache.getVersion());
                return ticketDetailCache;
            }
        }

        return getTicketDetailDistributedCache(id);
    }

    private TicketDetailCache getTicketDetailDistributedCache(Long ticketId) {
        // 1 - get data
        TicketDetailCache ticketDetailCache = redisInfrasService.getObject(genEventItemKey(ticketId), TicketDetailCache.class);
        if (ticketDetailCache == null) {
            ticketDetailCache = getTicketDetailDatabase(ticketId);
        }
        // 2 - put data to local cache
        ticketDetailLocalCache.put(ticketId, ticketDetailCache);
        log.info("GET TICKET FROM DISTRIBUTED LOCK");
        return ticketDetailCache;
    }

    public TicketDetailCache getTicketDetailDatabase(Long ticketId) {
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock(genEventItemKeyLock(ticketId));
    
        try {
            // 1 - Create lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Init empty ticketDetailCache
            TicketDetailCache ticketDetailCache = new TicketDetailCache();

            // CAUTION: ALTHOUGH SUCCESS OR NOT MUST BE UNLOCK FOR ANOTHER THREAD
            if (!isLock) {
                return ticketDetailCache; // Data is lock, retry later
            }
            // Re-check after acquiring the lock (it might have been loaded by another thread during the lock)
           ticketDetailCache = redisInfrasService.getObject(genEventItemKey(ticketId), TicketDetailCache.class);
            // 2. YES -> get data from cache
           if (ticketDetailCache != null) {
               return ticketDetailCache;
           }
            // 3. NO -> get data from db
            log.info("GET TICKET FORM DBS");
            TicketDetail ticketDetail = ticketDetailDomainService.getTicketDetailById(ticketId);
            if (ticketDetail == null) {
                // set data to distributed cache is empty, agaist ddos database
                redisInfrasService.setObject(genEventItemKey(ticketId), ticketDetailCache);
                return ticketDetailCache;
            }
            ticketDetailCache = new TicketDetailCache().withClone(ticketDetail).withVersion(System.currentTimeMillis());
            log.info("GET TICKET FORM DBS {}", ticketDetailCache);
            // set data to distributed cache
            redisInfrasService.setObject(genEventItemKey(ticketId), ticketDetailCache);
            return ticketDetailCache;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    private TicketDetailCache getTicketDetailLocalCache(Long id) {
        try {
            return ticketDetailLocalCache.getIfPresent(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String genEventItemKey(Long ticketId) {
        return "TICKET:ITEM:" + ticketId;
    }

    private String genEventItemKeyLock(Long ticketId) {
        return "TICKET:LOCK:" + ticketId;
    }
}
