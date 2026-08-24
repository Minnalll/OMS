package com.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer productId;
    private String productName;
    private String description;
    @Column(precision = 6, scale = 2)
    private BigDecimal price;
    private int availableQuantity;
}
