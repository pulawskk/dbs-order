package com.pulawskk.dbsorder.service;

import com.pulawskk.dbsorder.api.v1.mapper.OrderMapper;
import com.pulawskk.dbsorder.api.v1.model.OrderDto;
import com.pulawskk.dbsorder.api.v1.model.OrderListDto;
import com.pulawskk.dbsorder.controller.ResourceNotFoundException;
import com.pulawskk.dbsorder.controller.v1.OrderController;
import com.pulawskk.dbsorder.domain.Order;
import com.pulawskk.dbsorder.domain.OrderStatus;
import com.pulawskk.dbsorder.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String getOrderUrl() {
        return OrderController.ORDER_BASE_URL + "/";
    }

    @Override
    public OrderListDto findAllOrdersDto() {
        return new OrderListDto(orderRepository.findAll().stream()
                .map(OrderMapper.INSTANCE::orderToOrderDto)
                .map(o -> {
                    o.setOrderUrl(getOrderUrl() + o.getId());
                    o.setUserId(o.getUserId());
                    return o;})
                .collect(Collectors.toList()));
    }

    @Override
    public OrderListDto findAllOrdersDtoByUserId(Long id) {
//        return new OrderListDto(orderRepository.findAllByUserId(id).stream()
//                .map(OrderMapper.INSTANCE::orderToOrderDto)
//                .map(o -> {
//                    o.setOrderUrl(getOrderUrl() + o.getId());
//                    o.setUserId(o.getUserId());
//                    return o;})
//                .collect(Collectors.toList()));
        return null;
    }

    @Override
    public OrderDto findOrderById(Long id) {
        Order orderFromDb = orderRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Order with id: " + id + " does not exist."));
        OrderDto orderDto = OrderMapper.INSTANCE.orderToOrderDto(orderFromDb);
        orderDto.setOrderUrl(getOrderUrl() + orderFromDb.getId());
        return orderDto;
    }

    @Override
    public OrderDto createNewOrder(OrderDto orderDto) {
        Order orderToBeSaved = OrderMapper.INSTANCE.orderDtoToOrder(orderDto);
        orderToBeSaved.setPlacedAt(LocalDateTime.now());
        orderToBeSaved.setOrderStatus(OrderStatus.PLACED);

        Order savedOrder = orderRepository.save(orderToBeSaved);

        OrderDto savedOrderDto = OrderMapper.INSTANCE.orderToOrderDto(savedOrder);
        savedOrderDto.setOrderUrl(getOrderUrl() + savedOrder.getId());

        return savedOrderDto;
    }

    @Override
    public OrderDto patchOrder(Long id, OrderDto orderDto) {
        OrderDto actualOrder = OrderMapper.INSTANCE.orderToOrderDto(
                orderRepository.findById(id).orElseThrow(ResourceNotFoundException::new));

        Optional.ofNullable(orderDto.getDeliveryName()).ifPresent(o -> {
            if (!o.isBlank()) {
                actualOrder.setDeliveryName(o);
            }
        });

        Optional.ofNullable(orderDto.getDeliveryState()).ifPresent(o -> {
            if (!o.isBlank()) {
                actualOrder.setDeliveryState(o);
            }
        });

        Optional.ofNullable(orderDto.getDeliveryCity()).ifPresent(o -> {
            if (!o.isBlank()) {
                actualOrder.setDeliveryCity(o);
            }
        });

        Optional.ofNullable(orderDto.getDeliveryStreet()).ifPresent(o -> {
            if (!o.isBlank()) {
                actualOrder.setDeliveryStreet(o);
            }
        });

        Optional.ofNullable(orderDto.getDeliveryZIP()).ifPresent(o -> {
            if (!o.isBlank()) {
                actualOrder.setDeliveryZIP(o);
            }
        });

        Optional.ofNullable(orderDto.getCcNumber()).ifPresent(o -> {
            if (!o.isBlank()) {
                actualOrder.setCcNumber(o);
            }
        });

        Optional.ofNullable(orderDto.getCcExpiration()).ifPresent(o -> {
            if (!o.isBlank()) {
                actualOrder.setCcExpiration(o);
            }
        });

        Optional.ofNullable(orderDto.getCcCVV()).ifPresent(o -> {
            if (!o.isBlank()) {
                actualOrder.setCcCVV(o);
            }
        });

        Optional.ofNullable(orderDto.getOrderStatus()).ifPresent(o -> {
            if (!o.isBlank()) {
                actualOrder.setCcCVV(o);
            }
        });

        Order savedOrder = orderRepository.save(OrderMapper.INSTANCE.orderDtoToOrder(actualOrder));

        return OrderMapper.INSTANCE.orderToOrderDto(savedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
