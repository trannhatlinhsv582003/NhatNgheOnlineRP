package com.poly.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DTO.DayPoint;
import com.poly.Service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/api/charts")
@RequiredArgsConstructor
public class AdminMetricsApi {

    private final AnalyticsService analytics;

    @GetMapping("/orders-revenue-alltime")
    public List<DayPoint> ordersRevenueAllTime() {
        return analytics.ordersRevenueAllTime();
    }
}
