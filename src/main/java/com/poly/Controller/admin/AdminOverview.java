package com.poly.Controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.Service.OrderService;
import com.poly.Service.ProductService;
import com.poly.Service.ReviewService;
import com.poly.Service.CategoryService;
import com.poly.Model.Category;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminOverview {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String index(Model model) {
        var revenue = orderService.getTotalRevenue();
        var totalSold = orderService.getTotalSoldProducts();
        var totalCancelled = orderService.getTotalCancelledProducts();
        var topProducts = productService.getTopSellingProducts(10);
        var reviews = reviewService.getRecentReviews(5);

        // Dữ liệu cho biểu đồ hình tròn - thống kê theo danh mục
        List<Category> categories = categoryService.findAll();
        List<String> categoryNames = categories.stream()
            .map(Category::getCategoryName)
            .collect(Collectors.toList());
        
        List<Long> categoryCounts = categories.stream()
            .map(category -> productService.countByCategory(category.getCategoryID()))
            .collect(Collectors.toList());

        // Dữ liệu cho biểu đồ trạng thái đơn hàng
        List<Map<String, Object>> orderStatusStats = orderService.getOrderStatusStatistics();
        List<String> orderStatusLabels = orderStatusStats.stream()
            .map(stat -> (String) stat.get("status"))
            .collect(Collectors.toList());
        List<Long> orderStatusCounts = orderStatusStats.stream()
            .map(stat -> (Long) stat.get("count"))
            .collect(Collectors.toList());

        model.addAttribute("totalRevenue", revenue);
        model.addAttribute("totalSoldProducts", totalSold);
        model.addAttribute("totalCancelledProducts", totalCancelled);
        model.addAttribute("topSellingProducts", topProducts);
        model.addAttribute("recentReviews", reviews);
        
        // Dữ liệu cho biểu đồ
        model.addAttribute("categoryNames", categoryNames);
        model.addAttribute("categoryCounts", categoryCounts);
        model.addAttribute("orderStatusLabels", orderStatusLabels);
        model.addAttribute("orderStatusCounts", orderStatusCounts);

        return "admin/index";
    }
}
