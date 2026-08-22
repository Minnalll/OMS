package com.productservice.controller;

import com.productservice.dto.ProductAvailabilityResponse;
import com.productservice.model.Product;
import com.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Integer id) {
        return productService.getProduct(id);
    }

    @PostMapping("/")
    public Product createProduct(@RequestBody Product product){ return productService.createProduct(product); }

    @GetMapping("/")
    public List<Product> getProduct() {
        return productService.getProducts();
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/{id}/availability")
    public ProductAvailabilityResponse checkAvailability(@PathVariable Integer id, @RequestParam Integer quantity){
        return productService.checkAvailability(id, quantity);
    }

    @PutMapping("/qty")
    public String updateAvailabilityAfterOrder(@RequestParam Integer id, @RequestParam Integer quantity) {
        return productService.updateAvailabilityAfterOrder(id, quantity);
    }
}
