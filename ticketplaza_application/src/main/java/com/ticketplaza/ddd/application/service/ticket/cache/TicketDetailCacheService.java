package com.ticketplaza.ddd.application.service.ticket.cache;

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

    public TicketDetail getTicketDefaultCache(Long id, Long version) {
        log.info("Implement getTicketDefaultCache ->, {}, {}", id, version);
        TicketDetail ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);

        // 2. YES -> Cache hit
        if (ticketDetail != null) {
            log.info("FROM CACHE EXIST {}", ticketDetail);
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
                return ticketDetail;
            }

            // 3. NO -> Miss cache
            ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
            log.info("FROM DBS ->>> {}, {}", ticketDetail, version);
            if (ticketDetail == null) {
                log.info("TICKET NOT EXIST....{}", version);
                // Set cache
                redisInfrasService.setObject(genEventItemKey(id), null);
                return null;
            }

            redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
            return ticketDetail;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    private String genEventItemKey(Long itemId) {
        return "TICKET:ITEM:" +  itemId;
    }
}
