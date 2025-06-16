package com.ticketplaza.ddd.controller.http;

import com.ticketplaza.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplaza.ddd.controller.model.enums.ResultUtil;
import com.ticketplaza.ddd.controller.model.vo.ResultMessage;
import com.ticketplaza.ddd.domain.model.entity.TicketDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketDetailController {
    // Call Service Application
    @Autowired
    private TicketDetailAppService ticketDetailAppService;

    @GetMapping("/{ticketId}/detail/{detailId}")
    public ResultMessage<TicketDetail> getTicketDetail(
        @PathVariable("ticketId") Long ticketId,
        @PathVariable("detailId") Long detailId
    ) {
        log.info("TicketId: {}, detailId: {}", ticketId, detailId);
        return ResultUtil.data(ticketDetailAppService.getTicketDetailById(detailId));
    }
}
