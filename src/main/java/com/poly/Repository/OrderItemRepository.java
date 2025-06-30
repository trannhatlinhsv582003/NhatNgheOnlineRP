// OrderItemRepository.java
package com.poly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	List<OrderItem> findByOrder_OrderID(Integer orderId);
}
