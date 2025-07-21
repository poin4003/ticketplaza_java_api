package com.ticketplaza.ddd.controller.http;

import com.ticketplaza.ddd.application.model.TicketOrderDTO;
import com.ticketplaza.ddd.application.service.order.TicketOrderAppService;
import com.ticketplaza.ddd.controller.model.enums.ResultUtil;
import com.ticketplaza.ddd.controller.model.vo.ResultMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
public class TicketOrderController {
    private final TicketOrderAppService ticketOrderAppService;

    @GetMapping("/{ticketId}/{quantity}/order")
    public boolean orderTicket(
            @PathVariable("ticketId") Long ticketId,
            @PathVariable("quantity") int quantity
    ) {
        log.info("Controller:-> orderTicketByLevel | {}, {}", ticketId, quantity);
        return ticketOrderAppService.decreaseStock(ticketId, quantity);
    }

    @GetMapping("/{userId}/list")
    public ResultMessage<List<TicketOrderDTO>> getListOrderByUser(
            @PathVariable("userId") Long userId,
            @RequestParam("ntable") String ntable
    ) {
        log.info("Controller:-> getLIstOrderByUser | {}, {}", userId, ntable);
        return ResultUtil.data(ticketOrderAppService.findAll(ntable));
    }

    @GetMapping("/{userId}/{orderNumber}")
    public ResultMessage<TicketOrderDTO> getOrderByUser(
            @PathVariable("userId") Long userId,
            @PathVariable("orderNumber") String orderNumber,
            @RequestParam("ntable") String ntable
    ) {
        log.info("Controller:-> getOrderByUser | {}, {}", userId, orderNumber);
        return ResultUtil.data(ticketOrderAppService.findByOrderNumber(ntable, orderNumber));
    }
}
