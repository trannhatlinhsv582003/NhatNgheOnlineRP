// OrderItemService.java
package com.poly.Service;

import java.util.List;
import java.util.Optional;

import com.poly.Model.OrderItem;

public interface OrderItemService {
	List<OrderItem> findAll();

	Optional<OrderItem> findById(Integer id);

	OrderItem save(OrderItem item);

	void deleteById(Integer id);
}
