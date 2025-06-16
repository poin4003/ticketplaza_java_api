package com.ticketplaza.ddd.application.service.ticket;

import com.ticketplaza.ddd.domain.model.entity.TicketDetail;

public interface TicketDetailAppService {
    TicketDetail getTicketDetailById(Long ticketId);
}
