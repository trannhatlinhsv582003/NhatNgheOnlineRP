package com.poly.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@GetMapping
	public String index() {
		return "admin/index";
	}

	@GetMapping("/account")
	public String account() {
		return "admin/account/index";
	}

	@GetMapping("/role")
	public String role() {
		return "admin/role/index";
	}

	@GetMapping("/product")
	public String product() {
		return "admin/product/index";
	}

	@GetMapping("/category")
	public String category() {
		return "admin/category/index";
	}

	@GetMapping("/order")
	public String order() {
		return "admin/order/index";
	}
}
