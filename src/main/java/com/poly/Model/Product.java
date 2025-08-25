package com.poly.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID")
    private Integer productID;

    @Column(name = "ProductName", nullable = false)
    private String productName;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "BrandName")
    private String brandName;

    @Column(name = "Specifications", columnDefinition = "NVARCHAR(MAX)")
    private String specifications;

    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    @Column(name = "ImageURL")
    private String imageURL;

    @Column(name = "StockQuantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "CategoryID", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductImage> images;
}
