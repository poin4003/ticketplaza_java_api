package com.ticketplaza.ddd.controller.http;

import com.ticketplaza.ddd.application.model.TicketDetailDTO;
import com.ticketplaza.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplaza.ddd.controller.model.enums.ResultUtil;
import com.ticketplaza.ddd.controller.model.vo.ResultMessage;
import com.ticketplaza.ddd.domain.model.entity.TicketDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketDetailController {
    // Call Service Application
    @Autowired
    private TicketDetailAppService ticketDetailAppService;

    @GetMapping("/{ticketId}/detail/{detailId}")
    public ResultMessage<TicketDetailDTO> getTicketDetail(
        @PathVariable("ticketId") Long ticketId,
        @PathVariable("detailId") Long detailId,
        @RequestParam(name = "version", required = false) Long version
    ) {
        log.info("TicketId: {}, detailId: {}", ticketId, detailId);
        return ResultUtil.data(ticketDetailAppService.getTicketDetailById(detailId, version));
    }

    @GetMapping("/{ticketId}")
    public boolean orderTicketByUser(
            @PathVariable("ticketId") Long ticketId
    ) {
        return ticketDetailAppService.orderTicketByUser(ticketId);
    }
}
