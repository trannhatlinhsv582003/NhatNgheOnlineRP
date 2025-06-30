// ReviewServiceImpl.java
package com.poly.Service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.Model.Review;
import com.poly.Model.User;
import com.poly.Repository.ReviewRepository;
import com.poly.Repository.UserRepository;
import com.poly.Service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private UserRepository userRepository;

	public List<Review> findAll() {
		return reviewRepository.findAll();
	}

	public Optional<Review> findById(Integer id) {
		return reviewRepository.findById(id);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<Review> getApprovedReviewsByProduct(Integer productId) {
		return reviewRepository.findApprovedReviewsByProductId(productId);
	}

	public Review save(Review review) {
		return reviewRepository.save(review);
	}

	public void deleteById(Integer id) {
		reviewRepository.deleteById(id);
	}
}
