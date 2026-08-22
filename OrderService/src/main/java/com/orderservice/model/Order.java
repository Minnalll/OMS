package com.orderservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(unique = true)
    private Long orderId;
    private Integer productId;
    private Integer quantity;
    private String customerName;
    private LocalDateTime orderTimeStamp;
    private String orderStatus;
}
