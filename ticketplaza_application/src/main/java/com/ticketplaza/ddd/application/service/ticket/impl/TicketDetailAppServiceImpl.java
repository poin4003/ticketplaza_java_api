package com.ticketplaza.ddd.application.service.ticket.impl;


import com.ticketplaza.ddd.application.mapper.TicketDetailMapper;
import com.ticketplaza.ddd.application.model.TicketDetailDTO;
import com.ticketplaza.ddd.application.model.cache.TicketDetailCache;
import com.ticketplaza.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplaza.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.ticketplaza.ddd.domain.model.entity.TicketDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailAppServiceImpl implements TicketDetailAppService {
    @Autowired
    private TicketDetailCacheService ticketDetailCacheService;

    @Override
    public TicketDetailDTO getTicketDetailById(Long ticketId, Long version) {
        log.info("Implement Application: {}, {}", ticketId, version);
        TicketDetailCache ticketDetailCache = ticketDetailCacheService.getTicketDetail(ticketId, version);
        if (ticketDetailCache == null) {
            log.warn("Ticket Detail Cache is empty for ID {}", ticketId);
            return null;
        }
        // mapper to DTO
        TicketDetailDTO ticketDetailDTO = TicketDetailMapper.mapperToTicketDetailDTO(ticketDetailCache.getTicketDetail());
        if (ticketDetailDTO == null) {
            log.warn("Ticket Detail DTO is null for ID {}", ticketId);
            return null;
        }
        ticketDetailDTO.setVersion(ticketDetailCache.getVersion());
        return ticketDetailDTO;
    }

    @Override
    public boolean orderTicketByUser(Long ticketId) {
        return ticketDetailCacheService.orderTicketByUser(ticketId);
    }
}
