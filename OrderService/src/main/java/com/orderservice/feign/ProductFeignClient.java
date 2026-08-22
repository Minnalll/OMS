package com.orderservice.feign;

import com.orderservice.dto.ProductAvailabilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ProductService")
public interface ProductFeignClient {

    @GetMapping("/products/{id}/availability")
    ProductAvailabilityResponse checkAvailability(@PathVariable("id") Integer id, @RequestParam("quantity") Integer quantity);

    @PutMapping("/products/qty")
    public String updateProductAvailability(@RequestParam("id") Integer id, @RequestParam("quantity") Integer quantity);
}
