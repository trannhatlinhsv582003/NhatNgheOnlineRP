// CartServiceImpl.java
package com.poly.Service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.Model.Cart;
import com.poly.Model.Order;
import com.poly.Model.OrderItem;
import com.poly.Model.Product;
import com.poly.Model.User;
import com.poly.Repository.CartRepository;
import com.poly.Repository.OrderItemRepository;
import com.poly.Repository.OrderRepository;
import com.poly.Repository.ProductRepository;
import com.poly.Service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;

	public List<Cart> findAll() {
		return cartRepository.findAll();
	}

	public Optional<Cart> findById(Integer id) {
		return cartRepository.findById(id);
	}

	public Cart save(Cart cart) {
		return cartRepository.save(cart);
	}

	public void deleteById(Integer id) {
		cartRepository.deleteById(id);
	}

	// cart
	@Override
	public List<Cart> getCartItems(User user) {
		return cartRepository.findByUser(user);
	}

	@Override
	@Transactional
	public void clearCart(User user) {
		cartRepository.deleteByUser(user);
	}

	@Override
	public double getTotalPrice(User user) {
		List<Cart> cartItems = getCartItems(user);
		return cartItems.stream()
				.map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
	}

	@Override
	public void addToCart(User user, Integer productId, Integer quantity) {
		Product product = productRepository.findById(productId).orElseThrow();

		// Tìm cart item đã có chưa
		Cart existing = cartRepository.findByUserAndProduct(user, product);

		if (existing != null) {
			existing.setQuantity(existing.getQuantity() + quantity);
			existing.setAddedAt(LocalDateTime.now());
			cartRepository.save(existing);
		} else {
			Cart newItem = new Cart();
			newItem.setUser(user);
			newItem.setProduct(product);
			newItem.setQuantity(quantity);
			newItem.setAddedAt(LocalDateTime.now());
			cartRepository.save(newItem);
		}
	}

	public int createOrder(User user, String fullName, String phone, String email, String address, String note,
			String shippingMethod, boolean invoice, List<Cart> cartItems) {
// Tạo đơn hàng
		Order order = new Order();
		order.setUser(user);
		order.setShippingAddress(address);
		order.setStatus("Chờ thanh toán");
		order.setTrackingCode(UUID.randomUUID().toString()); // Mã tracking ngẫu nhiên
		orderRepository.save(order);

		// Thêm các item
		for (Cart item : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setPrice(item.getProduct().getPrice());
			orderItemRepository.save(orderItem);
		}

		// Xoá cart
		cartRepository.deleteByUser(user);

		return order.getOrderID(); // trả về để redirect đến bước tiếp theo
	}

	@Override
	public void updateQuantity(User user, Integer productId, Integer quantity) {
		Cart item = cartRepository.findByUserAndProduct_ProductID(user, productId);
		if (item != null && quantity > 0) {
			item.setQuantity(quantity);
			cartRepository.save(item);
		}
	}

	@Override
	@Transactional
	public void removeFromCart(User user, Integer productId) {
		cartRepository.deleteByUserAndProduct_ProductID(user, productId);
	}

}
