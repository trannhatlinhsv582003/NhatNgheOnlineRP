// CategoryRepository.java
package com.poly.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	// Optional: tìm danh mục theo tên
	Category findByCategoryName(String name);
}
