package com.oms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;

    private Integer productId;

    private Integer quantity;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String orderStatus;

}
