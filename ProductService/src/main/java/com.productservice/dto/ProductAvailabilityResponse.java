package com.productservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ProductAvailabilityResponse {
    private Integer productId;
    private boolean available;
    private Integer quantity;
}
