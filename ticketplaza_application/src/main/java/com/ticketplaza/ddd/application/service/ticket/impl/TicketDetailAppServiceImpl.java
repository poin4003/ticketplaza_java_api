package com.ticketplaza.ddd.application.service.ticket.impl;


import com.ticketplaza.ddd.application.mapper.TicketDetailMapper;
import com.ticketplaza.ddd.application.model.TicketDetailDTO;
import com.ticketplaza.ddd.application.model.cache.TicketDetailCache;
import com.ticketplaza.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplaza.ddd.application.service.ticket.cache.TicketDetailCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketDetailAppServiceImpl implements TicketDetailAppService {
    private final TicketDetailCacheService ticketDetailCacheService;

    @Override
    public TicketDetailDTO getTicketDetailById(Long ticketId, Long version) {
        TicketDetailCache ticketDetailCache = ticketDetailCacheService.getTicketDetail(ticketId, version);

        // mapper to DTO
        TicketDetailDTO ticketDetailDTO = TicketDetailMapper.mapperToTicketDetailDTO(ticketDetailCache.getTicketDetail());
        ticketDetailDTO.setVersion(ticketDetailCache.getVersion());
        return ticketDetailDTO;
    }
}
