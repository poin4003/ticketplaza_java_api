package com.ticketplaza.ddd.application.service.ticket;

import com.ticketplaza.ddd.application.model.TicketDetailDTO;
import com.ticketplaza.ddd.domain.model.entity.TicketDetail;

public interface TicketDetailAppService {
    TicketDetailDTO getTicketDetailById(Long ticketId, Long version);
    boolean orderTicketByUser(Long ticketId);
}
