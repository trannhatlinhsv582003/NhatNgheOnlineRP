package com.poly.Service;

import java.util.List;

import com.poly.Model.Product;
import com.poly.Model.ProductImage;

public interface ProductImageService {
	List<ProductImage> getImagesByProduct(Product product);

	ProductImage save(ProductImage image);

	void deleteById(Integer id);
}
