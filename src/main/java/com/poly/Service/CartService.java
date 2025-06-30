// CartService.java
package com.poly.Service;

import java.util.List;
import java.util.Optional;

import com.poly.Model.Cart;
import com.poly.Model.User;

public interface CartService {
	List<Cart> findAll();

	Optional<Cart> findById(Integer id);

	Cart save(Cart cart);

	void deleteById(Integer id);

	// cart
	List<Cart> getCartItems(User user);

	void clearCart(User user);

	void addToCart(User user, Integer productId, Integer quantity);

	void updateQuantity(User user, Integer productId, Integer quantity);

	void removeFromCart(User user, Integer productId);

	double getTotalPrice(User user);

	int createOrder(User user, String fullName, String phone, String email, String address, String note,
			String shippingMethod, boolean invoice, List<Cart> cartItems);
}
