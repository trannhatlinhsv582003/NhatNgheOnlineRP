// OrderRepository.java
package com.poly.Repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.Model.Order;
import com.poly.Model.User;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser_UserID(Integer userId);

    Page<Order> findByUserFullNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("""
    SELECT SUM(oi.quantity * oi.price)
    FROM OrderItem oi
    JOIN oi.order o
    WHERE o.status IN ('Delivered', 'Paid')
""")
    BigDecimal getTotalRevenue();

    List<Order> findByShipper(User shipper);
}
