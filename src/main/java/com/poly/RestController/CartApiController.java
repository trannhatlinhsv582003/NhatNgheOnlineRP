package com.poly.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.Model.User;
import com.poly.Security.CustomUserDetails;
import com.poly.Service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

	@Autowired
	private CartService cartService;

	@PostMapping("/add")
	public String addToCart(@RequestParam Integer productId, @RequestParam Integer quantity,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) {
			return "unauthorized";
		}

		User currentUser = userDetails.getUser();
		cartService.addToCart(currentUser, productId, quantity);
		return "success";
	}
}
