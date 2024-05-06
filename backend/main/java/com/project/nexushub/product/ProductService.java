package com.project.nexushub.product;

import com.project.nexushub.category.CategoryService;
import com.project.nexushub.category.CategoryResponse;
import com.project.nexushub.category.Category;
import com.project.nexushub.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAllProducts();
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        CategoryResponse categoryResponse = mapToCategoryResponse(product.getCategory());
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProduct_name())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStock_quantity())
                .category(categoryResponse)
                .build();
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategory_name())
                .build();
    }

    public ProductResponse getProductByProductId(int productId) {
        Optional<Product> productOptional = productRepository.findProductByProductId(productId);
        Product product = productOptional.orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return mapToProductResponse(product);
    }

    public Product addProduct(AddProductRequest addProductRequest) {

        Optional<CategoryResponse> categoryOptional = categoryService.getCategoryByCategoryId(addProductRequest.getCategoryId());

        if (categoryOptional.isPresent()) {
            CategoryResponse categoryResponse = categoryOptional.get();
            Category category = new Category();
            category.setCategoryId(categoryResponse.getCategoryId());
            category.setCategory_name(categoryResponse.getCategoryName());

//            byte[] imageBytes = null;
//            try {
//                imageBytes = addProductRequest.getImage().getBytes();
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to read image file", e);
//            }

            Product product = Product.builder()
                    .product_name(addProductRequest.getProductName())
                    .category(category)
                    .description(addProductRequest.getDescription())
                    .stock_quantity(addProductRequest.getStock_quantity())
                    .price(addProductRequest.getPrice())
//                    .image(imageBytes)

                    .build();

            return productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Category not found");
        }
    }

    public ResponseEntity<AddProductRequest> updatingProduct(Integer productId, AddProductRequest updatedProductRequest) {
        try {
               Optional<Product> existingProductOptional = productRepository.findById(productId);

            if (existingProductOptional.isPresent()) {
                Product existingProduct = existingProductOptional.get();
                existingProduct.setProduct_name(updatedProductRequest.getProductName());
                Optional<Category> categoryOptional = categoryRepository.findById(updatedProductRequest.getCategoryId());

                if (categoryOptional.isPresent()) {
                    Category category = categoryOptional.get();
                    existingProduct.setCategory(category);
                } else {
                    return ResponseEntity.badRequest().build();
                }

                existingProduct.setDescription(updatedProductRequest.getDescription());
                existingProduct.setStock_quantity(updatedProductRequest.getStock_quantity());
                existingProduct.setPrice(updatedProductRequest.getPrice());

                productRepository.save(existingProduct);

                return ResponseEntity.ok(updatedProductRequest);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void deletingProduct(Integer productId) {
        Optional<Product> existingProductOptional = productRepository.findById(productId);

        if (existingProductOptional.isPresent()) {
            try {
                productRepository.deleteById(productId);
                ResponseEntity.ok("Product deleted successfully");
            } catch (Exception e) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete product");
            }
        } else {
            ResponseEntity.notFound().build();
        }
    }
}
