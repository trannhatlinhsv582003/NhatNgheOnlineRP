// ReviewRepository.java
package com.poly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.Model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	List<Review> findByProduct_ProductID(Integer productId);

	List<Review> findByProductProductIDAndApprovedTrue(Integer productId);

	@Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.product.productID = :productId AND r.approved = true ORDER BY r.createdAt DESC")
	List<Review> findApprovedReviewsByProductId(@Param("productId") Integer productId); // Lấy các đánh giá đã duyệt
}
