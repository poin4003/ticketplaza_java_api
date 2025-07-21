package com.ticketplaza.ddd.domain.repository;

import com.ticketplaza.ddd.domain.model.entity.TicketOrder;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDeductionRepository {
    void insertOrder(String yearMonth, TicketOrder ticketOrder);
    List<Object[]> findAll(String yearMonth);
    Object[] findByOrderNumber(String yearMonth, String orderNumber);
    List<Object[]> findByDateRange(String yearMonth, LocalDateTime startDate, LocalDateTime endDate);
}