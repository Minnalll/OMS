package com.orderservice.controller;

import com.orderservice.model.Order;
import com.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id){
        return orderService.getOrder(id);
    }

    @GetMapping("/")
    public List<Order> getAllOrder(){
        return orderService.getAllOrders();
    }

    /*@PostMapping("/")
    public Order createOrder(@RequestBody Order order){
       return orderService.createOrder(order);
    }*/

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order){
        return orderService.updateOrder(id, order);
    }

    @PostMapping("/ord")
    public String placeOrder(@RequestBody Order order) {
        return orderService.placeOrder(order);
    }
}
