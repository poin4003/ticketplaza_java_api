package com.ticketplaza.ddd.application.service.ticket;

import com.ticketplaza.ddd.application.model.TicketDetailDTO;

public interface TicketDetailAppService {
    TicketDetailDTO getTicketDetailById(Long ticketId, Long version);
}
