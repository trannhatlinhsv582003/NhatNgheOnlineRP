// ReviewRepository.java
package com.poly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	List<Review> findByProduct_ProductID(Integer productId);

	List<Review> findByApprovedTrue(); // Lấy các đánh giá đã duyệt
}
