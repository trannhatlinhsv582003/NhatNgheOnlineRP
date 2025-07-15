package com.poly.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.poly.Model.Product;

public interface ProductService {
	List<Product> findAll();

	Optional<Product> findById(Integer id);

	Product save(Product product);

	void deleteById(Integer id);
	
	Page<Product> findAll(Pageable pageable);
}
