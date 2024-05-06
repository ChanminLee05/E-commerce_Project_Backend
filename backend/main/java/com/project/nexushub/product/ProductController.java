package com.project.nexushub.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/product/add")
    public ResponseEntity<Product> addProduct(@RequestBody AddProductRequest addProductRequest) {
        Product addedProduct = productService.addProduct(addProductRequest);
        if (addedProduct != null) {
            return ResponseEntity.ok(addedProduct);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/product/update/{productId}")
    public ResponseEntity<AddProductRequest> updateProduct(@PathVariable Integer productId, @RequestBody AddProductRequest updatedProductRequest) {
        try {
            return productService.updatingProduct(productId, updatedProductRequest);
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
}
