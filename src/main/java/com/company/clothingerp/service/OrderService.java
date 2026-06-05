package com.company.clothingerp.service;

import com.company.clothingerp.model.*;
import com.company.clothingerp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByType(String type) {
        return orderRepository.findByType(type);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        // Calculate total amount
        double total = 0;
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
            total += item.getPrice() * item.getQuantity();
        }
        order.setTotalAmount(total);
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        
        Order savedOrder = orderRepository.save(order);

        // Adjust inventory inside the WMS
        List<Warehouse> warehouses = warehouseRepository.findAll();
        if (!warehouses.isEmpty()) {
            Warehouse defaultWarehouse = warehouses.get(0); // Use primary warehouse as default for processing
            for (OrderItem item : order.getOrderItems()) {
                int delta = order.getType().equalsIgnoreCase("SALE") ? -item.getQuantity() : item.getQuantity();
                
                Optional<Inventory> optInv = inventoryRepository.findByProductIdAndWarehouseId(item.getProduct().getId(), defaultWarehouse.getId());
                if (optInv.isPresent()) {
                    Inventory inv = optInv.get();
                    inv.setQuantity(Math.max(0, inv.getQuantity() + delta));
                    inventoryRepository.save(inv);
                } else if (delta > 0) {
                    Inventory inv = Inventory.builder()
                            .product(item.getProduct())
                            .warehouse(defaultWarehouse)
                            .quantity(delta)
                            .minThreshold(10)
                            .build();
                    inventoryRepository.save(inv);
                }
            }
        }

        return savedOrder;
    }

    public Double getTotalRevenue() {
        Double val = orderRepository.getTotalRevenue();
        return val != null ? val : 0.0;
    }

    public Double getTotalExpenses() {
        Double val = orderRepository.getTotalExpenses();
        return val != null ? val : 0.0;
    }

    public Long getSalesCount() {
        Long val = orderRepository.getSalesCount();
        return val != null ? val : 0L;
    }
}
