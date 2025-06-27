package com.poly.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.Model.Category;
import com.poly.Model.Product;
import com.poly.Repository.CategoryRepository;
import com.poly.Repository.ProductRepository;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Integer id, Model model) {
		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return "redirect:/"; // hoặc trả về trang lỗi
		}

		List<Product> relatedProducts = productRepository
				.findTop4ByCategoryCategoryIDAndProductIDNot(product.getCategory().getCategoryID(), id);

		model.addAttribute("product", product);
		model.addAttribute("relatedProducts", relatedProducts);

		return "product/detail";
	}

	@GetMapping("/category/{id}")
	public String category(@PathVariable("id") Integer categoryId,
			@RequestParam(value = "brand", required = false) String brand,
			@RequestParam(value = "sort", required = false) String sort, Model model) {

		List<Product> products;

		if (brand != null && !brand.isEmpty()) {
			if ("asc".equals(sort)) {
				products = productRepository.findByCategoryCategoryIDAndBrandNameOrderByPriceAsc(categoryId, brand);
			} else if ("desc".equals(sort)) {
				products = productRepository.findByCategoryCategoryIDAndBrandNameOrderByPriceDesc(categoryId, brand);
			} else {
				products = productRepository.findByCategoryCategoryIDAndBrandName(categoryId, brand);
			}
		} else {
			if ("asc".equals(sort)) {
				products = productRepository.findByCategoryCategoryIDOrderByPriceAsc(categoryId);
			} else if ("desc".equals(sort)) {
				products = productRepository.findByCategoryCategoryIDOrderByPriceDesc(categoryId);
			} else {
				products = productRepository.findByCategoryCategoryID(categoryId);
			}
		}

		// Tên danh mục
		String categoryName = categoryRepository.findById(categoryId).map(Category::getCategoryName)
				.orElse("Danh mục không tồn tại");

		// Gửi danh sách brand để render filter
		List<String> brands = productRepository.findDistinctBrandNamesByCategoryId(categoryId);

		model.addAttribute("products", products);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("selectedBrand", brand);
		model.addAttribute("selectedSort", sort);
		model.addAttribute("brands", brands);

		return "product/category";
	}

}
