package com.ticketplaza.ddd.infrastructure.persistence.repository;

import com.ticketplaza.ddd.domain.repository.TicketOrderRepository;
import com.ticketplaza.ddd.infrastructure.persistence.mapper.TicketOrderJPAMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketOrderRepositoryInfrasImpl implements TicketOrderRepository {
    private final TicketOrderJPAMapper ticketOrderJPAMapper;

    @Override
    public boolean decreaseStock(Long ticketId, int oldStockAvailable, int quantity) {
        log.info("Run test: decreaseStock with: | {}, {} ", ticketId, quantity);
        return ticketOrderJPAMapper.decreaseStock(ticketId, oldStockAvailable, quantity) > 0;
    }

    @Override
    public int getStockAvailable(Long ticketId) {
        return ticketOrderJPAMapper.getStockAvailable(ticketId);
    }
}
