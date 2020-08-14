package com.pulawskk.dbsorder.service;

import com.pulawskk.dbsorder.api.v1.model.OrderDto;
import com.pulawskk.dbsorder.api.v1.model.OrderListDto;

public interface OrderService {
    OrderListDto findAllOrdersDto();

    OrderListDto findAllOrdersDtoByUserId(Long id);

    OrderDto findOrderById(Long id);

    OrderDto createNewOrder(OrderDto orderDto);

    OrderDto patchOrder(Long id, OrderDto orderDto);

    void deleteOrder(Long id);
}
