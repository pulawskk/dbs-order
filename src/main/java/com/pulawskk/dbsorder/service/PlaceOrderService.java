package com.pulawskk.dbsorder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulawskk.dbsorder.api.v1.model.OrderDto;
import com.pulawskk.dbsorder.config.JmsConfig;
import com.pulawskk.dbsorder.model.OrderForDeliveryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Slf4j
@Component
public class PlaceOrderService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public PlaceOrderService(JmsTemplate jmsTemplate, ObjectMapper objectMapper, OrderService orderService) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @JmsListener(destination = JmsConfig.QUEUE_PLACE_ORDER)
    public void listenForOrder(@Headers MessageHeaders headers,
                       Message message) throws JMSException, JsonProcessingException {

        OrderDto orderDto = objectMapper.readValue(message.getBody(String.class), OrderDto.class);

        OrderDto savedOrderDto = orderService.createNewOrder(orderDto);
        log.debug("[ORDER] Received order name: " + savedOrderDto.getDeliveryName()
                + " with id: " + savedOrderDto.getId());

        OrderForDeliveryEvent orderForDeliveryEvent = OrderForDeliveryEvent.builder()
                .orderId(savedOrderDto.getId().toString())
                .deliveryId("")
                .orderStatus(savedOrderDto.getOrderStatus())
                .burgersAmount("1")
                .estimatedTimeOfArrival("")
                .build();

        subscribeToDeliveryStack(orderForDeliveryEvent);
    }

    private void subscribeToDeliveryStack(OrderForDeliveryEvent orderForDeliveryEvent) throws JsonProcessingException {
        Object event = objectMapper.writeValueAsString(orderForDeliveryEvent);
        jmsTemplate.convertAndSend(JmsConfig.QUEUE_DELIVERY_STACK, event);
    }

    @JmsListener(destination = JmsConfig.QUEUE_DELIVERY_STACK)
    private void listenForOrderInStack(Message message) throws JMSException, JsonProcessingException {

        OrderForDeliveryEvent orderForDeliveryEvent = objectMapper.readValue(message.getBody(String.class), OrderForDeliveryEvent.class);

        Message messageConfirmed = jmsTemplate.sendAndReceive(JmsConfig.QUEUE_DELIVERY_STATUS, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                Message confirmMessage = null;
                try {
                    confirmMessage = session.createTextMessage(objectMapper.writeValueAsString(orderForDeliveryEvent));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return confirmMessage;
            }
        });

        OrderForDeliveryEvent orderForDeliveryEventConfirmed =
                objectMapper.readValue(messageConfirmed.getBody(String.class), OrderForDeliveryEvent.class);

        log.debug("[ORDER] ETA for order id: {} = {} min",
                orderForDeliveryEventConfirmed.getOrderId(),
                orderForDeliveryEventConfirmed.getEstimatedTimeOfArrival());

        OrderDto orderDto = OrderDto.builder()
                .orderStatus("DELIVERING")
                .build();

        orderService.patchOrder(Long.parseLong(orderForDeliveryEventConfirmed.getOrderId()), orderDto);
    }

    @JmsListener(destination = JmsConfig.QUEUE_DELIVERY_CONFIRMED)
    private void listenForOrderConfirmed(Message message) throws JMSException, JsonProcessingException {
        OrderForDeliveryEvent event = objectMapper.readValue(message.getBody(String.class), OrderForDeliveryEvent.class);

        OrderDto orderDto = OrderDto.builder()
                .orderStatus("DELIVERED")
                .build();

        OrderDto patchedORderDto = orderService.patchOrder(Long.parseLong(event.getOrderId()), orderDto);
        log.debug("[ORDER] Order with id: {} has been delivered!",
                patchedORderDto.getId());
    }
}
