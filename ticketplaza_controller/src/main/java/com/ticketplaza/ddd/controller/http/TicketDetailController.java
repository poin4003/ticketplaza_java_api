package com.ticketplaza.ddd.controller.http;

import com.ticketplaza.ddd.application.model.TicketDetailDTO;
import com.ticketplaza.ddd.application.service.ticket.TicketDetailAppService;
import com.ticketplaza.ddd.controller.model.enums.ResultUtil;
import com.ticketplaza.ddd.controller.model.vo.ResultMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@Slf4j
@RequiredArgsConstructor
public class TicketDetailController {
    // Call Service Application
    private final TicketDetailAppService ticketDetailAppService;

    /**
     * Get ticket detail
     * @param ticketId
     * @param detailId
     * @param version
     * @return ResultUtil
     */
    @GetMapping("/{ticketId}/detail/{detailId}")
    public ResultMessage<TicketDetailDTO> getTicketDetail(
        @PathVariable("ticketId") Long ticketId,
        @PathVariable("detailId") Long detailId,
        @RequestParam(name = "version", required = false) Long version
    ) {
        return ResultUtil.data(ticketDetailAppService.getTicketDetailById(detailId, version));
    }
}
