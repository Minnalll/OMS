package com.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreatedEvent {

    private Long orderId;
    private Integer productId;
    private Integer quantity;
    private String customerName;
    private String orderStatus;

}
