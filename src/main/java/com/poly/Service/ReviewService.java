// ReviewService.java
package com.poly.Service;

import java.util.List;
import java.util.Optional;

import com.poly.Model.Review;

public interface ReviewService {
	List<Review> findAll();

	Optional<Review> findById(Integer id);

	Review save(Review review);

	void deleteById(Integer id);
}
