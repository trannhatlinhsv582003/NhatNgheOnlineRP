package com.poly.Controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.Model.Order;
import com.poly.Service.OrderService;

@Controller
@RequestMapping("/qr")
public class QRPayController {

	private final OrderService orderService;

	public QRPayController(OrderService orderService) {
		this.orderService = orderService;
	}

	@Value("${qrpay.bankCode}")
	private String bankCode;

	@Value("${qrpay.accountNumber}")
	private String accountNumber;

	@GetMapping("/checkout")
	public String checkout(@RequestParam("orderId") Integer orderId, Model model) {
		Order order = orderService.findById(orderId).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

		double amount = order.getTotalAmount().doubleValue();

		// Nội dung chuyển khoản: tên + danh sách sản phẩm
		String customerName = order.getUser().getFullName();
		String productList = order.getOrderItems().stream()
				.map(item -> item.getProduct().getProductID() + "×" + item.getQuantity())
				.collect(Collectors.joining("-"));

		String info = "Thanh toan tu " + customerName + " - SP: " + productList;
		String encodedInfo = URLEncoder.encode(info, StandardCharsets.UTF_8);

		String qrUrl = String.format("https://img.vietqr.io/image/%s-%s-compact2.png?amount=%.2f&addInfo=%s", bankCode,
				accountNumber, amount, encodedInfo);

		model.addAttribute("qrUrl", qrUrl);
		model.addAttribute("order", order);
		model.addAttribute("info", info);

		return "cart/qrpay";
	}
}
