// CartRepository.java
package com.poly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.Cart;
import com.poly.Model.Product;
import com.poly.Model.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	List<Cart> findByUser_UserID(Integer userId);

	List<Cart> findByUser(User user);

	void deleteByUser(User user);

	Cart findByUserAndProduct(User user, Product product);

	Cart findByUserAndProduct_ProductID(User user, Integer productId);

	void deleteByUserAndProduct_ProductID(User user, Integer productId);
}
