package com.poly.Service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.Model.Product;
import com.poly.Model.ProductImage;
import com.poly.Repository.ProductImageRepository;
import com.poly.Service.ProductImageService;

@Service
public class ProductImageServiceImpl implements ProductImageService {

	@Autowired
	private ProductImageRepository productImageRepository;

	@Override
	public List<ProductImage> getImagesByProduct(Product product) {
		return productImageRepository.findByProduct(product);
	}

	@Override
	public ProductImage save(ProductImage image) {
		return productImageRepository.save(image);
	}

	@Override
	public void deleteById(Integer id) {
		productImageRepository.deleteById(id);
	}
}
