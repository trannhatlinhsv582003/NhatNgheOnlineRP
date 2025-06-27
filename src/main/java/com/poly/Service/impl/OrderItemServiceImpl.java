// OrderItemServiceImpl.java
package com.poly.Service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.Model.OrderItem;
import com.poly.Repository.OrderItemRepository;
import com.poly.Service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {
	@Autowired
	private OrderItemRepository repo;

	public List<OrderItem> findAll() {
		return repo.findAll();
	}

	public Optional<OrderItem> findById(Integer id) {
		return repo.findById(id);
	}

	public OrderItem save(OrderItem item) {
		return repo.save(item);
	}

	public void deleteById(Integer id) {
		repo.deleteById(id);
	}
}
