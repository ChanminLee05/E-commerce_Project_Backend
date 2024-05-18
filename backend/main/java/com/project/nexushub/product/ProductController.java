package com.project.nexushub.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/nexusHub")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/product")
    public List<ProductResponse> getAllProduct() {
        return productService.getAllProducts();
    }

    @GetMapping("/product/{product_id}")
    public ResponseEntity<ProductResponse> getProductByProductId(@PathVariable("product_id") Integer productId) {
        ProductResponse productResponse = productService.getProductByProductId(productId);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/product/totalProducts")
    public ResponseEntity<Long> getTotalProducts() {
        Long totalProducts = productRepository.count();
        return ResponseEntity.ok(totalProducts);
    }

    @GetMapping("/product/category/{category_id}")
    public ResponseEntity<List<ProductResponse>> getProductByCategoryId(@PathVariable("category_id") Integer categoryId) {
        List<ProductResponse> products = productService.getAllProductsByCategoryId(categoryId);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/product/add")
    public ResponseEntity<?> addProduct(@RequestParam("productName") String productName,
                                        @RequestParam("brand") String brand,
                                        @RequestParam("categoryId") int categoryId,
                                        @RequestParam("description") String description,
                                        @RequestParam("stock_quantity") int stockQuantity,
                                        @RequestParam("price") double price,
                                        @RequestParam(value = "imageUrl", required = false) List<String> imageUrl,
                                        @RequestParam(value = "image", required = false) List<MultipartFile> imageFiles) {
        try {
            AddProductRequest addProductRequest = createAddProductRequest(productName, brand, categoryId, description, stockQuantity, price, imageUrl, imageFiles);
            ProductResponse addedProduct = productService.addProduct(addProductRequest, imageFiles);
            return ResponseEntity.ok(addedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/product/update/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer productId,
                                                           @RequestParam("productName") String productName,
                                                           @RequestParam("brand") String brand,
                                                           @RequestParam("categoryId") int categoryId,
                                                           @RequestParam("description") String description,
                                                           @RequestParam("stock_quantity") int stockQuantity,
                                                           @RequestParam("price") double price,
                                                           @RequestParam(value = "imageUrl", required = false) List<String> imageUrl,
                                                           @RequestParam(value = "image", required = false) List<MultipartFile> imageFiles) {
        try {
            AddProductRequest updatedProductRequest = createAddProductRequest(productName, brand, categoryId, description, stockQuantity, price, imageUrl, imageFiles);
            return productService.updatingProduct(productId, updatedProductRequest, imageFiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/product/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
        try {
            productService.deletingProduct(productId);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete product");
        }
    }

    private AddProductRequest createAddProductRequest(String productName, String brand, int categoryId, String description, int stockQuantity, double price, List<String> imageUrl, List<MultipartFile> imageFiles) {
        AddProductRequest addProductRequest = new AddProductRequest();
        addProductRequest.setProductName(productName);
        addProductRequest.setBrand(brand);
        addProductRequest.setCategoryId(categoryId);
        addProductRequest.setDescription(description);
        addProductRequest.setStock_quantity(stockQuantity);
        addProductRequest.setPrice(price);
        addProductRequest.setImageUrl(imageUrl);
        addProductRequest.setImageFile(imageFiles);
        return addProductRequest;
    }
}
