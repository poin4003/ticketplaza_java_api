package com.ticketplaza.ddd.infrastructure.persistence.repository;

import com.ticketplaza.ddd.domain.model.entity.TicketOrder;
import com.ticketplaza.ddd.domain.repository.OrderDeductionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDeductionInfrasRepositoryImpl implements OrderDeductionRepository {
    private final EntityManager entityManager;
    private final static String tablePrefix = "ticket_order_";

    private String getTableName(String monthOrder) {
        return tablePrefix + monthOrder;
    }

    @Override
    public void insertOrder(String yearMonth, TicketOrder ticketOrder) {
        String tableName = getTableName(yearMonth);
        String sql = "INSERT INTO " + tableName + " (order_number, user_id, total_amount, terminal_id, order_date, order_notes, updated_at, created_at)" +
                "VALUES (:orderNumber, :userId, :totalAmount, :terminalId, :orderDate, :orderNotes, :updatedAt, :createdAt)";

        LocalDateTime now = LocalDateTime.now();
        ticketOrder.setOrderDate(now);
        ticketOrder.setUpdatedAt(now);
        ticketOrder.setCreatedAt(now);

        entityManager.createNativeQuery(sql)
                .setParameter("orderNumber", ticketOrder.getOrderNumber())
                .setParameter("userId", ticketOrder.getUserId())
                .setParameter("totalAmount", ticketOrder.getTotalAmount())
                .setParameter("terminalId", ticketOrder.getTerminalId())
                .setParameter("orderDate", ticketOrder.getOrderDate())
                .setParameter("orderNotes", ticketOrder.getOrderNotes())
                .setParameter("updatedAt", ticketOrder.getUpdatedAt())
                .setParameter("createdAt", ticketOrder.getCreatedAt())
                .executeUpdate();
    }

    @Override
    public List<Object[]> findAll(String yearMonth) {
        String tableName = getTableName(yearMonth);
        String sql = "SELECT * FROM " + tableName;
        return entityManager.createNativeQuery(sql).getResultList();
    }

    @Override
    public Object[] findByOrderNumber(String yearMonth, String orderNumber) {
        String tableName = getTableName(yearMonth);
        String sql = "SELECT * FROM " + tableName + " WHERE order_number = :orderNumber";
        List<Object[]>  resultList = entityManager.createNativeQuery(sql)
                .setParameter("orderNumber", orderNumber)
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    @Override
    public List<Object[]> findByDateRange(String yearMonth, LocalDateTime startDate, LocalDateTime endDate) {
        String tableName = getTableName(yearMonth);
        String sql = "SELECT * FROM " + tableName + " WHERE order_date BETWEEN :startDate AND :endDate";
        return entityManager.createNativeQuery(sql)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}
