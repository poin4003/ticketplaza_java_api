package com.ticketplaza.ddd.domain.repository;

import com.ticketplaza.ddd.domain.model.entity.TicketDetail;

import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetail> findById(Long id);
}
