package com.poly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	// Tìm theo danh mục
	List<Product> findByCategory_CategoryID(Integer categoryId);

	// Lấy 8 sản phẩm ngẫu nhiên theo danh mục (dùng NEWID() cho SQL Server)
	@Query("SELECT p FROM Product p WHERE p.category.categoryName = :categoryName ORDER BY function('NEWID')")
	List<Product> findTop8ByCategoryNameRandom(@Param("categoryName") String categoryName);

	// Lấy 4 sản phẩm cùng danh mục, trừ chính nó
	List<Product> findTop4ByCategoryCategoryIDAndProductIDNot(Integer categoryId, Integer productId);

	// Danh sách sản phẩm theo danh mục
	List<Product> findByCategoryCategoryID(Integer categoryId);

	List<Product> findByCategoryCategoryIDOrderByPriceAsc(Integer categoryId);

	List<Product> findByCategoryCategoryIDOrderByPriceDesc(Integer categoryId);

	// Theo danh mục + brandName
	List<Product> findByCategoryCategoryIDAndBrandName(Integer categoryId, String brandName);

	List<Product> findByCategoryCategoryIDAndBrandNameOrderByPriceAsc(Integer categoryId, String brandName);

	List<Product> findByCategoryCategoryIDAndBrandNameOrderByPriceDesc(Integer categoryId, String brandName);

	// Lấy danh sách brandName duy nhất theo danh mục
	@Query("SELECT DISTINCT p.brandName FROM Product p WHERE p.category.categoryID = :categoryId")
	List<String> findDistinctBrandNamesByCategoryId(@Param("categoryId") Integer categoryId);

	// Tìm sản phẩm chứa từ khóa trong tên
	List<Product> findByProductNameContainingIgnoreCase(String keyword);

	// Theo keyword + brand
	List<Product> findByProductNameContainingIgnoreCaseAndBrandName(String keyword, String brandName);

	// Theo keyword + sắp xếp
	List<Product> findByProductNameContainingIgnoreCaseOrderByPriceAsc(String keyword);

	List<Product> findByProductNameContainingIgnoreCaseOrderByPriceDesc(String keyword);

	// Theo keyword + brand + sắp xếp
	List<Product> findByProductNameContainingIgnoreCaseAndBrandNameOrderByPriceAsc(String keyword, String brandName);

	List<Product> findByProductNameContainingIgnoreCaseAndBrandNameOrderByPriceDesc(String keyword, String brandName);

	// Lấy danh sách brand từ keyword
	@Query("SELECT DISTINCT p.brandName FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<String> findDistinctBrandNamesByKeyword(@Param("keyword") String keyword);

}
