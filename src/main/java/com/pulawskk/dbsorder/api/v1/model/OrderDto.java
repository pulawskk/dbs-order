package com.pulawskk.dbsorder.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDto {
    private Long id;

    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZIP;
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;
    private LocalDateTime placedAt;
    private String orderStatus;

    @JsonProperty(value = "order_url")
    private String orderUrl;

    private Long userId;

    private String burgerId;
}
