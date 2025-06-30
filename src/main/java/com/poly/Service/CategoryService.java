// CategoryService.java
package com.poly.Service;

import java.util.List;
import java.util.Optional;

import com.poly.Model.Category;

public interface CategoryService {
	List<Category> findAll();

	Optional<Category> findById(Integer id);

	Category save(Category category);

	void deleteById(Integer id);
}
