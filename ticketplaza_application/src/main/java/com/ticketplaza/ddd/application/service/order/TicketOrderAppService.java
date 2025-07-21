package com.ticketplaza.ddd.application.service.order;

import com.ticketplaza.ddd.application.model.TicketOrderDTO;
import com.ticketplaza.ddd.domain.model.entity.TicketOrder;

import java.util.List;

public interface TicketOrderAppService {
    boolean decreaseStock(Long ticketId, int quantity);

    int getStockAvailable(Long ticketId);

    // Order..
    List<TicketOrderDTO> findAll(String yearMonth);
    boolean insertOrder(String yearMonth, TicketOrder ticketOrder);
    TicketOrderDTO findByOrderNumber(String yearMonth, String orderNumber);
}
