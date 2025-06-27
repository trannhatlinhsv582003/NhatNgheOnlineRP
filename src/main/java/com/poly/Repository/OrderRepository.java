// OrderRepository.java
package com.poly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	List<Order> findByUser_UserID(Integer userId);
}
