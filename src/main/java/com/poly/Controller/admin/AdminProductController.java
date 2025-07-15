package com.poly.Controller.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.poly.Model.Product;
import com.poly.Model.ProductImage;
import com.poly.Service.CategoryService;
import com.poly.Service.ProductImageService;
import com.poly.Service.ProductService;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String productPage(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Product> productPage = productService.findAll(PageRequest.of(page, size, Sort.by("productID")));

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("pageSize", size);

        model.addAttribute("startPage", Math.max(0, page - 2));
        model.addAttribute("endPage", Math.min(productPage.getTotalPages() - 1, page + 2));

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("product", new Product());
        return "admin/product/index";
    }

    @ResponseBody
    @GetMapping("/api/{id}")
    public Product getProductJson(@PathVariable("id") Integer id) {
        return productService.findById(id).orElse(null);
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
            @RequestParam("imageFiles") MultipartFile[] imageFiles) throws IOException {
        // Xử lý thời gian tạo nếu là sản phẩm mới
        if (product.getProductID() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }

        // Lưu ảnh thumbnail nếu có
        if (!thumbnailFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + thumbnailFile.getOriginalFilename();
            String path = new ClassPathResource("static/images/products/").getFile().getAbsolutePath();
            thumbnailFile.transferTo(new File(path, fileName));
            product.setImageURL(fileName);
        } else {
            // ⚠️ Giữ lại ảnh cũ nếu không upload ảnh mới
            Optional<Product> existing = productService.findById(product.getProductID());
            existing.ifPresent(p -> product.setImageURL(p.getImageURL()));
        }

        // Lưu sản phẩm
        Product saved = productService.save(product);

        // Lưu ảnh chi tiết
        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    String path = new ClassPathResource("static/images/products/").getFile().getAbsolutePath();
                    file.transferTo(new File(path, fileName));

                    ProductImage img = new ProductImage();
                    img.setProduct(saved);
                    img.setImageURL(fileName);
                    productImageService.save(img);
                }
            }
        }

        return "redirect:/admin/product";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) throws IOException {
        Optional<Product> optional = productService.findById(id);
        if (optional.isPresent()) {
            Product product = optional.get();

            // Xóa ảnh phụ
            List<ProductImage> images = productImageService.getImagesByProduct(product);
            String path = new ClassPathResource("static/images/products/").getFile().getAbsolutePath();
            for (ProductImage img : images) {
                Files.deleteIfExists(Paths.get(path, img.getImageURL()));
                productImageService.deleteById(img.getImageID());
            }

            // Xóa ảnh thumbnail
            if (product.getImageURL() != null) {
                Files.deleteIfExists(Paths.get(path, product.getImageURL()));
            }

            // Xóa sản phẩm
            productService.deleteById(id);
        }

        return "redirect:/admin/product";
    }
}
