package com.company.clothingerp.repository;

import com.company.clothingerp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByType(String type);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findBySupplierId(Long supplierId);
    Optional<Order> findByOrderNumber(String orderNumber);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.type = 'SALE' AND o.status <> 'CANCELLED'")
    Double getTotalRevenue();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.type = 'PURCHASE' AND o.status <> 'CANCELLED'")
    Double getTotalExpenses();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.type = 'SALE'")
    Long getSalesCount();
}
