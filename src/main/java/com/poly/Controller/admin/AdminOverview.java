package com.poly.Controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.Service.OrderService;
import com.poly.Service.ProductService;
import com.poly.Service.ReviewService;

@Controller
@RequestMapping("/admin")
public class AdminOverview {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String index(Model model) {
        var revenue = orderService.getTotalRevenue();
        var totalSold = orderService.getTotalSoldProducts();
        var topProducts = productService.getTopSellingProducts(10);
        var reviews = reviewService.getRecentReviews(5);

        System.out.println("🧾 Doanh thu: " + revenue);
        System.out.println("📦 Đã bán: " + totalSold);
        System.out.println("🔥 Top sản phẩm: " + topProducts.size());
        System.out.println("⭐ Review gần đây: " + reviews.size());

        model.addAttribute("totalRevenue", revenue);
        model.addAttribute("totalSoldProducts", totalSold);
        model.addAttribute("topSellingProducts", topProducts);
        model.addAttribute("recentReviews", reviews);

        return "admin/index";
    }

}
