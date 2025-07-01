package com.poly.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/search")
public class CategoryController {
	@GetMapping("/category/{id}")
	public String category() {
		return ("category/index");
	}
}
