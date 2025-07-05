package com.poly.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.Model.Order;
import com.poly.Model.User;
import com.poly.Repository.OrderRepository;
import com.poly.Repository.UserRepository;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@GetMapping
	public String index(Model model) {
		// Lấy email user hiện tại từ security context
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User user = userRepository.findByEmail(email);
		model.addAttribute("user", user);
		return "account/profile";
	}

	@PostMapping
	public String updateProfile(@ModelAttribute("user") User formUser, Model model) {
		// Lấy user hiện tại từ DB
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User user = userRepository.findByEmail(email);
		if (user != null) {
			user.setFullName(formUser.getFullName());
			user.setGender(formUser.getGender());
			user.setPhone(formUser.getPhone());
			user.setBirthDay(formUser.getBirthDay());
			userRepository.save(user);
			model.addAttribute("user", user);
			model.addAttribute("success", true);
		}
		return "account/profile";
	}

	@GetMapping("/order-history")
	public String orderHistory(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User user = userRepository.findByEmail(email);
		List<Order> orders = orderRepository.findByUser_UserID(user.getUserID());
		model.addAttribute("orders", orders);
		model.addAttribute("user", user);
		return ("account/orderHistory");
	}

	// @GetMapping
	// public String index() {
	// return ("account/profile");
	// }
	//
	// @GetMapping("/order-history")
	// public String orderHistory() {
	//
	// return ("account/orderHistory");
	// }
}
