// src/main/java/com/poly/Repository/AnalyticsRepository.java
package com.poly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.Model.Order;

public interface AnalyticsRepository extends JpaRepository<Order, Integer> {

    @Query(value = """
        ;WITH bounds AS (
            SELECT CAST(MIN(o.OrderDate) AS date) AS start_date,
                   CAST(MAX(o.OrderDate) AS date) AS end_date
            FROM Orders o
            WHERE o.Status IN ('Paid','Delivered')
        ),
        d AS (
            SELECT start_date AS d, end_date FROM bounds
            UNION ALL
            SELECT DATEADD(day, 1, d), end_date FROM d WHERE d < end_date
        )
        SELECT
            CONVERT(varchar(10), d.d, 120) AS label,           -- YYYY-MM-DD
            COUNT(DISTINCT o.OrderID) AS orders,
            COALESCE(SUM(oi.Quantity * oi.Price), 0) AS revenue
        FROM d
        LEFT JOIN Orders o
               ON CAST(o.OrderDate AS date) = d.d
              AND o.Status IN ('Paid','Delivered')
        LEFT JOIN OrderItems oi
               ON oi.OrderID = o.OrderID
        GROUP BY d.d
        ORDER BY d.d
        OPTION (MAXRECURSION 32767);
        """, nativeQuery = true)
    List<Object[]> ordersRevenueAllTime();
}
