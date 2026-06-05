package com.company.clothingerp.controller.api;

import com.company.clothingerp.model.Order;
import com.company.clothingerp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Processing (ERP)", description = "Endpoints for creating and retrieving sales and procurement orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @Operation(summary = "Get list of all orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order details by ID")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Submit a new sale or purchase order (Adjusts WMS stock automatically)")
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }
}
