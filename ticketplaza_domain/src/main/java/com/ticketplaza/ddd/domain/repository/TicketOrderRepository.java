package com.ticketplaza.ddd.domain.repository;

public interface TicketOrderRepository {
    boolean decreaseStock(Long ticketId, int oldStockAvailable, int quantity);

    int getStockAvailable(Long ticketId);
}
