package com.orderservice.service;

import com.orderservice.dto.OrderCreatedEvent;
import com.orderservice.dto.ProductAvailabilityResponse;
import com.orderservice.exception.ResourceNotFoundException;
import com.orderservice.feign.ProductFeignClient;
import com.orderservice.kafka.OrderProducer;
import com.orderservice.model.Order;
import com.orderservice.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductFeignClient productClient;

    @Autowired
    private OrderProducer producer;

    public Order getOrder(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found with ID : " + id));
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    /*public Order createOrder(Order order) {
        return orderRepo.save(order);
    }*/

    public Order updateOrder(Long id, Order order) {
        Order existingOrder = getOrder(id);
        existingOrder.setQuantity(order.getQuantity() == null ? existingOrder.getQuantity() :  order.getQuantity());
        existingOrder.setProductId(order.getProductId() == null ? existingOrder.getProductId() :  order.getProductId());

        existingOrder.setCustomerName(order.getCustomerName() == null ? existingOrder.getCustomerName() :  order.getCustomerName());
        existingOrder.setOrderStatus(order.getOrderStatus() == null ? existingOrder.getOrderStatus() : order.getOrderStatus());

        return orderRepo.save(existingOrder);
    }

    public String placeOrder(Order order){

        ProductAvailabilityResponse response =
                productClient.checkAvailability(order.getProductId(), order.getQuantity());

        if(!response.isAvailable()){
            return "Product is Out of Stock";
        }
        order.setOrderId(Long.parseLong(
                ZonedDateTime.now(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));
        order.setOrderTimeStamp(LocalDateTime.now());
        order.setOrderStatus("Order Placed");
        order.setCustomerName(System.getProperty("user.name"));
        Order savedOrder = orderRepo.save(order);

        productClient.updateProductAvailability(savedOrder.getProductId(), savedOrder.getQuantity());

        OrderCreatedEvent event = new OrderCreatedEvent();

        event.setOrderId(savedOrder.getOrderId());
        event.setProductId(savedOrder.getProductId());
        event.setQuantity(savedOrder.getQuantity());
        event.setCustomerName(savedOrder.getCustomerName());
        event.setCustomerEmail(savedOrder.getCustomerEmail());
        event.setCustomerPhone(savedOrder.getCustomerPhone());
        event.setOrderStatus(savedOrder.getOrderStatus());

        producer.publish(event);
        return "Order Placed Successfully";
    }
}
