package com.poly.Service;

import java.util.List;
import java.util.Optional;

import com.poly.Model.Product;

public interface ProductService {
	List<Product> findAll();

	Optional<Product> findById(Integer id);

	Product save(Product product);

	void deleteById(Integer id);
}
