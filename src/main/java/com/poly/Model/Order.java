package com.poly.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderID;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    @JsonIgnoreProperties({
        "passwordHash", "email", "role", "birthDay", "createdAt",
        "lastLogin", "lastAction", "lastIP", "imageUrl"
    })
    private User user;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "OrderDate")
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String status;

    private String trackingCode;

    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "ShipperID")
    @JsonIgnoreProperties({
        "passwordHash", "email", "role", "birthDay", "createdAt",
        "lastLogin", "lastAction", "lastIP", "imageUrl"
    })
    private User shipper;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("order") // tránh vòng lặp ngược
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("order") // tránh vòng lặp ngược
    private Payment payment;

    public BigDecimal getTotalAmount() {
        if (orderItems == null || orderItems.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
