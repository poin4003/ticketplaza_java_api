package com.ticketplaza.ddd.application.service.ticket.impl;


import com.ticketplaza.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplaza.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.ticketplaza.ddd.domain.model.entity.TicketDetail;
import com.ticketplaza.ddd.domain.service.TicketDetailDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailAppServiceImpl implements TicketDetailAppService {
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    @Autowired
    private TicketDetailCacheService ticketDetailCacheService;

    @Override
    public TicketDetail getTicketDetailById(Long ticketId) {
        log.info("Implement Application: {}", ticketId);
        return ticketDetailCacheService.getTicketDefaultCache(ticketId, System.currentTimeMillis());
    }
}
