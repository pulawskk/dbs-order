package com.pulawskk.dbsorder.controller.v1;

import com.pulawskk.dbsorder.api.v1.model.OrderDto;
import com.pulawskk.dbsorder.api.v1.model.OrderListDto;
import com.pulawskk.dbsorder.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(OrderController.ORDER_BASE_URL)
@CrossOrigin
public class OrderController {
    public final static String ORDER_BASE_URL = "/api/v1/orders";

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderListDto displayAllOrders(@RequestParam(required = false) String orderStatus,
                                         @RequestParam(required = false) Long userId) {
        if (orderStatus != null && !orderStatus.isBlank()) {
            return orderService.findAllOrdersByOrderStatus(orderStatus);
        } else if (userId != null) {
            return orderService.findAllOrdersDtoByUserId(userId);
        }
        return orderService.findAllOrdersDto();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto displayOrderDtoById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto patchOrderDto(@RequestBody OrderDto orderDto, @PathVariable Long id) {
        return orderService.patchOrder(id, orderDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrderDto(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
