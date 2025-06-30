package com.poly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.Product;
import com.poly.Model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
	List<ProductImage> findByProduct(Product product);
}
