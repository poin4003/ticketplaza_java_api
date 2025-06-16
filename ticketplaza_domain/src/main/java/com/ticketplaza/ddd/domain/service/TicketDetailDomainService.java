package com.ticketplaza.ddd.domain.service;

import com.ticketplaza.ddd.domain.model.entity.TicketDetail;

public interface TicketDetailDomainService {
    TicketDetail getTicketDetailById(Long ticketId);
}
