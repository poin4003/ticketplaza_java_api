package com.ticketplaza.ddd.domain.service;

public interface TicketOrderDomainService {
    boolean decreaseStock(Long ticketId, int oldStockAvailable, int quantity);

    int getStockAvailable(Long ticketId);
}
