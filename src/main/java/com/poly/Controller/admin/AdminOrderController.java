package com.poly.Controller.admin;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poly.DTO.OrderDTO;
import com.poly.DTO.OrderItemDTO;
import com.poly.DTO.ProductDTO;
import com.poly.DTO.UserDTO;
import com.poly.Model.Order;
import com.poly.Service.OrderService;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String orderPage(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Order> orderPage = orderService.findAll(PageRequest.of(page, size, Sort.by("orderDate")));

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("pageSize", size);

        model.addAttribute("startPage", Math.max(0, page - 2));
        model.addAttribute("endPage", Math.min(orderPage.getTotalPages() - 1, page + 2));

        model.addAttribute("order", new Order());
        return "admin/order/index";
    }

    @ResponseBody
    @GetMapping("/api/{id}")
    public OrderDTO getOrderJson(@PathVariable("id") Integer id) {
        Optional<Order> optional = orderService.findById(id);
        if (optional.isEmpty()) {
            return null;
        }

        Order order = optional.get();

        OrderDTO dto = new OrderDTO();
        dto.setOrderID(order.getOrderID());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setOrderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(order.getUser().getFullName());
        dto.setUser(userDTO);

        List<OrderItemDTO> items = order.getOrderItems().stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());

            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductName(item.getProduct().getProductName());
            itemDTO.setProduct(productDTO);

            return itemDTO;
        }).toList();

        dto.setOrderItems(items);

        return dto;
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Integer orderID, @RequestParam String status) {
        Optional<Order> opt = orderService.findById(orderID);
        opt.ifPresent(order -> {
            order.setStatus(status);
            orderService.save(order);
        });
        return "redirect:/admin/order";
    }
}
