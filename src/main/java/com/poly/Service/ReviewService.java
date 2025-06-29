// ReviewService.java
package com.poly.Service;

import java.util.List;
import java.util.Optional;

import com.poly.Model.Review;
import com.poly.Model.User;

public interface ReviewService {
	List<Review> findAll();

	List<Review> getApprovedReviewsByProduct(Integer productId);

	Optional<Review> findById(Integer id);

	User findByEmail(String email);

	Review save(Review review);

	void deleteById(Integer id);
}
