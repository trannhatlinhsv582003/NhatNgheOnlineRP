package com.poly.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.Model.Cart;
import com.poly.Model.Order;
import com.poly.Model.OrderItem;
import com.poly.Model.Payment;
import com.poly.Model.PaymentMethod;
import com.poly.Model.Product;
import com.poly.Model.User;
import com.poly.Security.CustomUserDetails;
import com.poly.Service.CartService;
import com.poly.Service.OrderService;
import com.poly.Service.PaymentMethodService;
import com.poly.Service.PaymentService;
import com.poly.Service.ProductService;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PaymentMethodService paymentMethodService;

	@Autowired
	private ProductService productService;

	// Bước 1: Giỏ hàng
	@GetMapping
	public String index(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		User currentUser = userDetails.getUser();
		List<Cart> cartItems = cartService.getCartItems(currentUser);
		double totalPrice = cartService.getTotalPrice(currentUser);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("totalPrice", totalPrice);
		return "cart/index";
	}

	// Bước 2: Xác nhận thông tin
	@GetMapping("/info")
	public String info(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		model.addAttribute("user", userDetails.getUser());
		double totalPrice = cartService.getTotalPrice(userDetails.getUser());
		model.addAttribute("totalPrice", totalPrice);
		return "cart/info";
	}

	@PostMapping("/info/next")
	public String infoNext(@ModelAttribute("user") User userForm, @RequestParam("shippingFee") int shippingFee,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("user", userForm);
		redirectAttributes.addFlashAttribute("shippingFee", shippingFee);
		return "redirect:/cart/checkout";
	}

	// Bước 3: Chọn phương thức thanh toán
	@GetMapping("/checkout")
	public String checkout(@AuthenticationPrincipal CustomUserDetails userDetails, Model model,
			@ModelAttribute("user") User userForm, @ModelAttribute("shippingFee") Integer shippingFee,
			@RequestParam(name = "isBuyNow", defaultValue = "false") boolean isBuyNow,
			@RequestParam(name = "buyNowOrderId", required = false) Integer buyNowOrderId) {
		double totalPrice;
		List<PaymentMethod> paymentMethods = paymentMethodService.findAll();

		if (isBuyNow && buyNowOrderId != null) {
			Order order = orderService.findById(buyNowOrderId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
			totalPrice = order.getTotalAmount().doubleValue();
			model.addAttribute("buyNowOrder", order);
		} else {
			User user = userDetails.getUser();
			totalPrice = cartService.getTotalPrice(user);
		}

		// Nếu shippingFee null (ví dụ mua ngay), dùng mặc định
		if (shippingFee == null) {
			shippingFee = 30000;
		}

		model.addAttribute("user", userForm);
		model.addAttribute("shippingFee", shippingFee);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("finalTotal", totalPrice + shippingFee);
		model.addAttribute("paymentMethods", paymentMethods);
		model.addAttribute("isBuyNow", isBuyNow);
		model.addAttribute("buyNowOrderId", buyNowOrderId);

		return "cart/checkout";
	}

	@PostMapping("/pay")
	@Transactional
	public String pay(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute("user") User userForm,
			@RequestParam("paymentMethod") String paymentCode,
			@RequestParam(name = "note", required = false) String note,
			@RequestParam(name = "shippingMethod", required = false) String shippingMethod,
			@RequestParam(name = "invoice", defaultValue = "false") boolean invoice,
			@RequestParam(name = "buyNowOrderId", required = false) Integer buyNowOrderId,
			@RequestParam(name = "isBuyNow", defaultValue = "false") boolean isBuyNow,
			RedirectAttributes redirectAttributes) {
		User currentUser = userDetails.getUser();
		Order order;

		// Trường hợp MUA NGAY
		if (isBuyNow && buyNowOrderId != null) {
			order = orderService.findById(buyNowOrderId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng mua ngay"));
		} else {
			// Trường hợp GIỎ HÀNG
			List<Cart> cartItems = cartService.getCartItems(currentUser);
			if (cartItems.isEmpty()) {
				return "redirect:/cart";
			}

			order = orderService.createOrder(currentUser, userForm.getAddress(), cartItems);

			for (Cart cart : cartItems) {
				Product product = cart.getProduct();
				OrderItem item = new OrderItem();
				item.setOrder(order);
				item.setProduct(product);
				item.setQuantity(cart.getQuantity());
				item.setPrice(product.getPrice());
				orderService.saveOrderItem(item);
			}

			// Xoá giỏ hàng
			cartService.clearCart(currentUser);
		}

		// Tạo bản ghi thanh toán
		PaymentMethod method = paymentMethodService.findByCode(paymentCode);
		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(method);
		payment.setPaymentDate(LocalDateTime.now());
		payment.setPaymentStatus("Chưa thanh toán");
		payment.setPaymentNote(note);
		paymentService.save(payment);

		// Điều hướng theo phương thức thanh toán
		if ("cod".equals(paymentCode)) {
			payment.setPaymentStatus("Đã thanh toán");
			paymentService.save(payment);
			return "redirect:/cart/result?orderId=" + order.getOrderID();
		} else if ("vnpay".equals(paymentCode)) {
			return "redirect:/vnpay/checkout?orderId=" + order.getOrderID();
		} else if ("qrpay".equals(paymentCode)) {
			return "redirect:/qr/checkout?orderId=" + order.getOrderID();
		}

		return "redirect:/cart";
	}

	// Bước 4: Kết quả
	@GetMapping("/result")
	@Transactional
	public String result(@RequestParam("orderId") Integer orderId, Model model) {
		Order order = orderService.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		Payment payment = paymentService.findByOrder(order);

		// Format ngày tại đây
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		String formattedDate = order.getOrderDate().format(formatter);

		model.addAttribute("order", order);
		model.addAttribute("payment", payment);
		model.addAttribute("formattedDate", formattedDate); // thêm dòng này
		return "cart/result";
	}

	// Nút mua ngay
	@GetMapping("/buy-now")
	public String buyNow(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestParam("productId") Integer productId,
			@RequestParam(name = "quantity", defaultValue = "1") Integer quantity,
			RedirectAttributes redirectAttributes) {
		Product product = productService.findById(productId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		// Tạo 1 đơn hàng tạm thời chỉ chứa sản phẩm này
		User user = userDetails.getUser();

		Order tempOrder = new Order();
		tempOrder.setUser(user);
		tempOrder.setOrderDate(LocalDateTime.now());
		tempOrder.setStatus("Chờ thanh toán");
		tempOrder.setShippingAddress(user.getAddress());
		orderService.save(tempOrder); // lưu để có orderId

		OrderItem item = new OrderItem();
		item.setOrder(tempOrder);
		item.setProduct(product);
		item.setQuantity(quantity);
		item.setPrice(product.getPrice());
		orderService.saveOrderItem(item);

		redirectAttributes.addFlashAttribute("user", user);
		return "redirect:/cart/checkout?isBuyNow=true&buyNowOrderId=" + tempOrder.getOrderID();

	}

	// Các API khác
	@PostMapping("/update")
	public String updateQuantity(@RequestParam("productId") Integer productId,
			@RequestParam("quantity") Integer quantity, @AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userDetails.getUser();
		cartService.updateQuantity(user, productId, quantity);
		return "redirect:/cart";
	}

	@PostMapping("/remove")
	public String removeFromCart(@RequestParam("productId") Integer productId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userDetails.getUser();
		cartService.removeFromCart(user, productId);
		return "redirect:/cart";
	}
}