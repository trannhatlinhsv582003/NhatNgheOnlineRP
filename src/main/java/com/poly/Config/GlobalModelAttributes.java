package com.poly.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.poly.Model.User;
import com.poly.Security.CustomUserDetails;
import com.poly.Service.CartService;

//tạo các biến global để dùng ở mọi file

@ControllerAdvice
public class GlobalModelAttributes {

	@Autowired
	private CartService cartService;

	// truyền current user đến mọi model
	@ModelAttribute
	public void addCurrentUser(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
			model.addAttribute("currentUser", userDetails.getUser());
		}
	}

	// truyền số lượng sản phẩm đến mọi model để hiển thị trên head nav
	@ModelAttribute
	public void addGlobalAttributes(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
			User currentUser = userDetails.getUser();

			model.addAttribute("currentUser", currentUser);

			// Lấy số lượng sản phẩm trong giỏ
			int cartItemCount = cartService.getCartItems(currentUser).stream().mapToInt(item -> item.getQuantity())
					.sum();

			model.addAttribute("cartItemCount", cartItemCount);
		} else {
			model.addAttribute("cartItemCount", 0); // chưa login
		}
	}
}
