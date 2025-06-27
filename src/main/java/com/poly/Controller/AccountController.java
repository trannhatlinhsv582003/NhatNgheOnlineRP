package com.poly.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
	@GetMapping
	public String index() {
		return ("account/profile");
	}

	@GetMapping("/order-history")
	public String orderHistory() {
		return ("account/orderHistory");
	}
}
