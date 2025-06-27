// ReviewServiceImpl.java
package com.poly.Service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.Model.Review;
import com.poly.Repository.ReviewRepository;
import com.poly.Service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewRepository repo;

	public List<Review> findAll() {
		return repo.findAll();
	}

	public Optional<Review> findById(Integer id) {
		return repo.findById(id);
	}

	public Review save(Review review) {
		return repo.save(review);
	}

	public void deleteById(Integer id) {
		repo.deleteById(id);
	}
}
