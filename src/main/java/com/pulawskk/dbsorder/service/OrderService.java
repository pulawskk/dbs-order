package com.pulawskk.dbsorder.service;

import com.pulawskk.dbsorder.api.v1.model.OrderDto;
import com.pulawskk.dbsorder.api.v1.model.OrderListDto;
import com.pulawskk.dbsorder.domain.OrderStatus;

public interface OrderService {
    OrderListDto findAllOrdersDto();

    OrderListDto findAllOrdersDtoByUserId(Long id);

    OrderListDto findAllOrdersByOrderStatus(String orderStatus);

    OrderDto findOrderById(Long id);

    OrderDto createNewOrder(OrderDto orderDto);

    OrderDto patchOrder(Long id, OrderDto orderDto);

    void deleteOrder(Long id);
}
