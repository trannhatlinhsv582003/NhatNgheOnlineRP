// src/main/java/com/poly/Service/impl/AnalyticsServiceImpl.java
package com.poly.Service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poly.DTO.DayPoint;
import com.poly.Repository.AnalyticsRepository;
import com.poly.Service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository repo;

    @Override
    public List<DayPoint> ordersRevenueAllTime() {
        return repo.ordersRevenueAllTime().stream()
                .map(r -> new DayPoint(
                (String) r[0],
                ((Number) r[1]).longValue(),
                ((Number) r[2]).doubleValue()))
                .toList();
    }
}
