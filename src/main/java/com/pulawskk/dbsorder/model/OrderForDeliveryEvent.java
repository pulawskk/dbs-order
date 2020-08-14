package com.pulawskk.dbsorder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class OrderForDeliveryEvent implements Serializable {

    private static final Long serialVersionUID = 12313123123L;

    private String orderId;
    private String deliveryId;
    private String orderStatus;
    private String burgersAmount;
    private String estimatedTimeOfArrival;
}
