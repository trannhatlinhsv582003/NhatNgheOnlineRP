// CategoryServiceImpl.java
package com.poly.Service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.Model.Category;
import com.poly.Repository.CategoryRepository;
import com.poly.Service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository repo;

	public List<Category> findAll() {
		return repo.findAll();
	}

	public Optional<Category> findById(Integer id) {
		return repo.findById(id);
	}

	public Category save(Category category) {
		return repo.save(category);
	}

	public void deleteById(Integer id) {
		repo.deleteById(id);
	}
}
