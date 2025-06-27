package com.poly.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.Model.Order;
import com.poly.Model.Payment;
import com.poly.Service.OrderService;
import com.poly.Service.PaymentService;
import com.poly.Util.VNPayUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/vnpay")
public class VNPayController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Value("${vnpay.tmnCode}")
	private String vnp_TmnCode;

	@Value("${vnpay.hashSecret}")
	private String vnp_HashSecret;

	@Value("${vnpay.payUrl}")
	private String vnp_PayUrl;

	@Value("${vnpay.returnUrl}")
	private String vnp_ReturnUrl;

	@GetMapping("/checkout")
	public void checkout(@RequestParam("orderId") Integer orderId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Order order = orderService.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

		long amount = order.getTotalAmount().multiply(BigDecimal.valueOf(100)).longValue(); // BigDecimal → VND

		String vnp_TxnRef = String.valueOf(orderId);
		String vnp_IpAddr = request.getHeader("X-FORWARDED-FOR");
		if (vnp_IpAddr == null) {
			vnp_IpAddr = request.getRemoteAddr();
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		String vnp_CreateDate = formatter.format(cld.getTime());

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());

		Map<String, String> vnpParams = new HashMap<>();
		vnpParams.put("vnp_Version", "2.1.0");
		vnpParams.put("vnp_Command", "pay");
		vnpParams.put("vnp_TmnCode", vnp_TmnCode);
		vnpParams.put("vnp_Amount", String.valueOf(amount));
		vnpParams.put("vnp_CurrCode", "VND");
		vnpParams.put("vnp_TxnRef", vnp_TxnRef);
		vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + vnp_TxnRef);
		vnpParams.put("vnp_OrderType", "other");
		vnpParams.put("vnp_Locale", "vn");
		vnpParams.put("vnp_ReturnUrl", vnp_ReturnUrl);
		vnpParams.put("vnp_IpAddr", vnp_IpAddr);
		vnpParams.put("vnp_CreateDate", vnp_CreateDate);
		vnpParams.put("vnp_ExpireDate", vnp_ExpireDate);
		vnpParams.put("vnp_BankCode", "NCB");

		// Build signed URL
		List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();

		for (int i = 0; i < fieldNames.size(); i++) {
			String key = fieldNames.get(i);
			String value = vnpParams.get(key);
			hashData.append(key).append("=").append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
			query.append(URLEncoder.encode(key, StandardCharsets.US_ASCII)).append("=")
					.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
			if (i < fieldNames.size() - 1) {
				hashData.append("&");
				query.append("&");
			}
		}

		String secureHash = VNPayUtil.hmacSHA512(vnp_HashSecret, hashData.toString());
		query.append("&vnp_SecureHash=").append(secureHash);

		String paymentUrl = vnp_PayUrl + "?" + query.toString();
		System.out.println("Redirect URL: " + paymentUrl);
		response.sendRedirect(paymentUrl);
	}

	// Sau khi thanh toán xong
	@GetMapping("/return")
	public String vnpReturn(@RequestParam Map<String, String> allParams, Model model) {
		String responseCode = allParams.get("vnp_ResponseCode");
		String txnRef = allParams.get("vnp_TxnRef");

		Order order = orderService.findById(Integer.parseInt(txnRef))
				.orElseThrow(() -> new RuntimeException("Order not found"));
		Payment payment = paymentService.findByOrder(order);

		if ("00".equals(responseCode)) {
			payment.setPaymentStatus("Đã thanh toán");
			paymentService.save(payment);
		}

		return "redirect:/cart/result?orderId=" + txnRef;
	}

}
