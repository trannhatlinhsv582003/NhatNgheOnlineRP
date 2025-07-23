package com.poly.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poly.Model.Order;
import com.poly.Model.User;
import com.poly.Service.OrderService;

@Controller
@RequestMapping("/shipper")
public class ShipperController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String index(@ModelAttribute("currentUser") User currentUser, Model model) {
        if (currentUser == null || !"Shipper".equals(currentUser.getRole())) {
            return "redirect:/";
        }

        List<Order> orders = orderService.findByShipper(currentUser);
        model.addAttribute("orders", orders);
        return "shipper/index";
    }

    @PostMapping("/update-status")
    @ResponseBody
    public ResponseEntity<String> updateOrderStatus(
            @RequestParam Integer orderID,
            @RequestParam String status,
            @ModelAttribute("currentUser") User currentUser) {

        if (currentUser == null || !"Shipper".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).body("Bạn không có quyền");
        }

        Optional<Order> optionalOrder = orderService.findById(orderID);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();
        if (order.getShipper() == null || !order.getShipper().getUserID().equals(currentUser.getUserID())) {
            return ResponseEntity.status(403).body("Không phải đơn của bạn");
        }

        order.setStatus(status);
        orderService.save(order);
        return ResponseEntity.ok("Cập nhật thành công");
    }
}
