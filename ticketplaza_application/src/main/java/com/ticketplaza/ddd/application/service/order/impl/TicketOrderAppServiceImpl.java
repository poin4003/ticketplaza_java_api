package com.ticketplaza.ddd.application.service.order.impl;

import com.ticketplaza.ddd.application.model.TicketOrderDTO;
import com.ticketplaza.ddd.application.service.order.TicketOrderAppService;
import com.ticketplaza.ddd.application.service.order.cache.StockOrderCacheService;
import com.ticketplaza.ddd.domain.model.entity.TicketOrder;
import com.ticketplaza.ddd.domain.service.OrderDeductionDomainService;
import com.ticketplaza.ddd.domain.service.TicketOrderDomainService;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketOrderAppServiceImpl implements TicketOrderAppService {
    private final TicketOrderDomainService ticketOrderDomainService;
    private final OrderDeductionDomainService orderDeductionDomainService;
    private final StockOrderCacheService stockOrderCacheService;

    @Override
    @Transactional
    public boolean decreaseStock(Long ticketId, int quantity) {
        boolean isDecreaseStockSuccess = false;
        try {
            int oldStockAvailable = stockOrderCacheService.decreaseStockCache(ticketId, quantity);
            if (oldStockAvailable == 0) {
                log.info("Case: oldStockAvailable is 0");
                // If oldStockAvailable = 0 or then StockInventory unavailable -> return to client
                return false;
            }

            // If oldStockAvailable available then continues stockDeduction in database
            isDecreaseStockSuccess = ticketOrderDomainService.decreaseStock(ticketId, oldStockAvailable, quantity);
            log.info("Case: isDecreaseStockSuccess {}", isDecreaseStockSuccess);
            if (isDecreaseStockSuccess) { // If stockDeduction is success then -> add order for user.
                // Add order
                TicketOrder ticketOrderPlace = new TicketOrder();
                //
                int userId = ThreadLocalRandom.current().nextInt(1, 10);
                ticketOrderPlace.setUserId(userId);
                ticketOrderPlace.setOrderNumber("OKX-SGN-" + userId + "-" + System.currentTimeMillis());
                ticketOrderPlace.setTotalAmount(new BigDecimal(quantity * 5000));
                ticketOrderPlace.setTerminalId("OKX-SGN");
                ticketOrderPlace.setOrderNotes("Order -> Pending");

                String nTable = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

                log.info("currently nTable ---> | {}", nTable);
                orderDeductionDomainService.insertOrder(nTable, ticketOrderPlace);
            }

            return true;
        } catch (PessimisticLockException e) {
            log.warn("Case: isDecreaseStockSuccess {}", isDecreaseStockSuccess);
            return false;
        } catch (LockTimeoutException e) {
            log.error("Lock timeout while processing ticketId = {}", ticketId, e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when decreasing stock for ticketId = {}", ticketId, e);
            return false;
        }
    }

    @Override
    public int getStockAvailable(Long ticketId) {
        return ticketOrderDomainService.getStockAvailable(ticketId);
    }

    @Override
    public List<TicketOrderDTO> findAll(String yearMonth) {
        List<Object[]> results = orderDeductionDomainService.findAll(yearMonth);
        return results.stream().map(row -> new TicketOrderDTO(
                (Integer) row[0],
                (Integer) row[1],
                (String) row[2],
                (BigDecimal) row[3],
                (String) row[4],
                ((Timestamp) row[5]).toLocalDateTime(),
                (String) row[6],
                ((Timestamp) row[7]).toLocalDateTime(),
                ((Timestamp) row[8]).toLocalDateTime()
            )).toList();
    }

    @Override
    public boolean insertOrder(String yearMonth, TicketOrder ticketOrder) {
        orderDeductionDomainService.insertOrder(yearMonth, ticketOrder);
        return true;
    }

    @Override
    public TicketOrderDTO findByOrderNumber(String yearMonth, String orderNumber) {
        log.info("nTable: findByOrderNumber = {}", yearMonth);
        // If not use yearMonth, use parse orderNumber by extractYearMonthFromOrderNumber()
        Object[] row = orderDeductionDomainService.findByOrderNumber(yearMonth, orderNumber);

        if (row == null) {
            return null;
        }

        return new TicketOrderDTO(
                ((Number) row[0]).intValue(),
                ((Number) row[1]).intValue(),
                (String) row[2],
                (BigDecimal) row[3],
                (String) row[4],
                ((Timestamp) row[5]).toLocalDateTime(),
                (String) row[6],
                ((Timestamp) row[7]).toLocalDateTime(),
                ((Timestamp) row[8]).toLocalDateTime()
        );
    }

    private String extractYearMonthFromOrderNumber(String orderNumber) {
        try {
            String[] parts = orderNumber.split("-");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid order number format");
            }
            long timestamp = Long.parseLong(parts[parts.length - 1]);

            LocalDateTime dateTime = Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // Format to yyyyMM
            return dateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract yearMonth from orderNumber: " + orderNumber, e);
        }
    }
}
