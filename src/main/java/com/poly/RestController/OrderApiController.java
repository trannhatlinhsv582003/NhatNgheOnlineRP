package com.poly.RestController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.Model.Order;
import com.poly.Model.User;
import com.poly.Service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @PathVariable Integer id,
            @ModelAttribute("currentUser") User currentUser) {

        Optional<Order> optionalOrder = orderService.findById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();

        if (order.getShipper() == null || !order.getShipper().getUserID().equals(currentUser.getUserID())) {
            return ResponseEntity.status(403).body("Không có quyền xem đơn này");
        }

        return ResponseEntity.ok(order);
    }
}
