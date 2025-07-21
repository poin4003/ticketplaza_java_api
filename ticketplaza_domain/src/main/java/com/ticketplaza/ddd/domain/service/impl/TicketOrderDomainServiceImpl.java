package com.ticketplaza.ddd.domain.service.impl;

import com.ticketplaza.ddd.domain.repository.TicketOrderRepository;
import com.ticketplaza.ddd.domain.service.TicketOrderDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketOrderDomainServiceImpl implements TicketOrderDomainService {
    private final TicketOrderRepository ticketOrderRepository;

    @Override
    public boolean decreaseStock(Long ticketId, int oldStockAvailable, int quantity) {
        return ticketOrderRepository.decreaseStock(ticketId, oldStockAvailable, quantity);
    }

    @Override
    public int getStockAvailable(Long ticketId) {
        return ticketOrderRepository.getStockAvailable(ticketId);
    }
}
