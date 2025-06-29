package com.poly.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poly.Model.Category;
import com.poly.Model.Product;
import com.poly.Model.Review;
import com.poly.Model.User;
import com.poly.Repository.CategoryRepository;
import com.poly.Repository.ProductRepository;
import com.poly.Service.ProductService;
import com.poly.Service.ReviewService;
import com.poly.Service.UserService;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private UserService userService;

	@GetMapping("/detail/{id}")
	public String showProductDetail(@PathVariable("id") Integer productId, Model model) {
		Product product = productService.findById(productId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		List<Product> relatedProducts = productRepository
				.findTop4ByCategoryCategoryIDAndProductIDNot(product.getCategory().getCategoryID(), productId);

		List<Review> reviews = reviewService.getApprovedReviewsByProduct(productId);

		// Tính tổng đánh giá và điểm trung bình
		int totalReviews = reviews.size();
		double averageRating = 0.0;
		if (totalReviews > 0) {
			int totalScore = reviews.stream().mapToInt(Review::getRating).sum();
			averageRating = (double) totalScore / totalReviews;
		}

		// Format ngày cho từng review
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		List<Map<String, Object>> formattedReviews = reviews.stream().map(r -> Map.of("user", r.getUser(), "rating",
				r.getRating(), "comment", r.getComment(), "createdAt", r.getCreatedAt().format(formatter)))
				.collect(Collectors.toList());

		// Gửi dữ liệu sang view
		model.addAttribute("product", product);
		model.addAttribute("relatedProducts", relatedProducts);
		model.addAttribute("reviews", formattedReviews);
		model.addAttribute("averageRating", averageRating);
		model.addAttribute("totalReviews", totalReviews);

		return "product/detail";
	}

	@PostMapping("/review")
	@ResponseBody
	public ResponseEntity<?> postReview(@RequestParam Integer productId, @RequestParam int rating,
			@RequestParam String comment, Principal principal) {

		if (principal == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
		}

		// Lấy user từ email
		User currentUser = userService.findByEmail(principal.getName());
		if (currentUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
		}

		Product product = productService.findById(productId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		Review review = new Review();
		review.setProduct(product);
		review.setUser(currentUser);
		review.setRating(rating);
		review.setComment(comment);
		review.setApproved(true); // không cần duyệt
		review.setCreatedAt(LocalDateTime.now());

		reviewService.save(review);
		return ResponseEntity.ok("success");
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
