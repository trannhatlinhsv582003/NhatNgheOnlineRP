// OrderServiceImpl.java
package com.poly.Service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.Model.Cart;
import com.poly.Model.Order;
import com.poly.Model.OrderItem;
import com.poly.Model.Product;
import com.poly.Model.User;
import com.poly.Repository.OrderItemRepository;
import com.poly.Repository.OrderRepository;
import com.poly.Repository.ProductRepository;
import com.poly.Service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ProductRepository productRepository;

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Optional<Order> findById(Integer id) {
		return orderRepository.findById(id);
	}

	public Order save(Order order) {
		return orderRepository.save(order);
	}

	public void deleteById(Integer id) {
		orderRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Order createOrder(User user, String shippingAddress, List<Cart> cartItems) {
		Order order = new Order();
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		order.setStatus("Chờ xử lý");
		order.setShippingAddress(shippingAddress);
		return orderRepository.save(order);
	}

	@Override
	public void saveOrderItem(OrderItem item) {
		// Giảm số lượng tồn kho
		Product product = item.getProduct();
		int currentStock = product.getStockQuantity();
		product.setStockQuantity(currentStock - item.getQuantity());
		productRepository.save(product);

		// Lưu OrderItem
		orderItemRepository.save(item);
	}
}
