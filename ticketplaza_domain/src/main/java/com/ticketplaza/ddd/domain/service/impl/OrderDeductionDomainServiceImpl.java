package com.ticketplaza.ddd.domain.service.impl;

import com.ticketplaza.ddd.domain.model.entity.TicketOrder;
import com.ticketplaza.ddd.domain.repository.OrderDeductionRepository;
import com.ticketplaza.ddd.domain.service.OrderDeductionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDeductionDomainServiceImpl implements OrderDeductionDomainService {
    private final OrderDeductionRepository orderDeductionRepository;

    @Override
    public void insertOrder(String yearMonth, TicketOrder ticketOrder) {
        orderDeductionRepository.insertOrder(yearMonth, ticketOrder);
    }

    @Override
    public List<Object[]> findAll(String yearMonth) {
        return orderDeductionRepository.findAll(yearMonth);
    }

    @Override
    public Object[] findByOrderNumber(String yearMonth, String orderNumber) {
        return orderDeductionRepository.findByOrderNumber(yearMonth, orderNumber);
    }

    @Override
    public List<Object[]> findByDateRange(String yearMonth, LocalDateTime startDate, LocalDateTime endDate) {
        return List.of();
    }
}
