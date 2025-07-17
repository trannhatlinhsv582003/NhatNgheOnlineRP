// OrderService.java
package com.poly.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.poly.Model.Cart;
import com.poly.Model.Order;
import com.poly.Model.OrderItem;
import com.poly.Model.User;

public interface OrderService {

    List<Order> findAll();

    Optional<Order> findById(Integer id);

    Order save(Order order);

    void deleteById(Integer id);

    Order createOrder(User user, String shippingAddress, List<Cart> cartItems);

    void saveOrderItem(OrderItem item);

    Page<Order> findAll(Pageable pageable);

    Page<Order> searchOrders(String keyword, Pageable pageable);

    BigDecimal getTotalRevenue();

    int getTotalSoldProducts();

    int getTotalCancelledProducts();
}
