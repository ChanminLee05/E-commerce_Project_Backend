package com.project.nexushub.product;

import com.project.nexushub.category.CategoryService;
import com.project.nexushub.category.CategoryResponse;
import com.project.nexushub.category.Category;
import com.project.nexushub.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
        products.forEach(product -> {
            System.out.println("Debug: Product ID: " + product.getProductId());
            System.out.println("Debug: Product Name: " + product.getProduct_name());
            System.out.println("Debug: Photos: " + product.getPhotos());
            System.out.println("Debug: Image URLs: " + product.getImageUrl());
        });
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        CategoryResponse categoryResponse = mapToCategoryResponse(product.getCategory());
        System.out.println("Mapping Product ID: " + product.getProductId());
        System.out.println("Photos: " + product.getPhotos());
        System.out.println("Image URLs: " + product.getImageUrl());
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProduct_name())
                .brand(product.getBrand())
                .description(product.getDescription())
                .price(product.getPrice())
                .photos(product.getPhotos())
                .stockQuantity(product.getStock_quantity())
                .category(categoryResponse)
                .imageUrl(product.getImageUrl())
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

    public List<ProductResponse> getAllProductsByCategoryId(int categoryId) {
        List<Product> products = productRepository.findAllbyCategoryId(categoryId);
        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public void saveProduct(Product product) {
        this.productRepository.save(product);
    }

    public ProductResponse addProduct(AddProductRequest addProductRequest, List<MultipartFile> imageFiles) {

        if (productRepository.findProductByProductName(addProductRequest.getProductName()).isPresent()) {
            throw new IllegalArgumentException("Product with the same title already exists");
        }

        Category category = getCategory(addProductRequest.getCategoryId());

        Product product = Product.builder()
                .product_name(addProductRequest.getProductName())
                .brand(addProductRequest.getBrand())
                .category(category)
                .description(addProductRequest.getDescription())
                .stock_quantity(addProductRequest.getStock_quantity())
                .price(addProductRequest.getPrice())
                .imageUrl(addProductRequest.getImageUrl() != null ? addProductRequest.getImageUrl() : null)
                .build();

        Product savedProduct = productRepository.save(product);

        if (addProductRequest.getImageUrl() == null && imageFiles != null) {
            saveProductImage(product, imageFiles);
            savedProduct = productRepository.save(savedProduct);
        } else if (addProductRequest.getImageUrl() == null && imageFiles == null) {
            throw new IllegalArgumentException("Image URL or image file must be provided");
        }


        return mapToProductResponse(savedProduct);
    }

    private Category getCategory(int categoryId) {
        return categoryService.getCategoryByCategoryId(categoryId)
                .map(categoryResponse -> {
                    Category category = new Category();
                    category.setCategoryId(categoryResponse.getCategoryId());
                    category.setCategory_name(categoryResponse.getCategoryName());
                    return category;
                })
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    private void saveProductImage(Product product, List<MultipartFile> imageFiles) {
        List<String> photoUrls = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            try {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
                String uploadDir = "C:/Users/Chanmin/chatbot_project/chatbot/frontend/public/images";
                FileUploadUtil.saveFile(uploadDir, fileName, imageFile);

                String photoUrl = "/images/" + fileName;
                photoUrls.add(photoUrl);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to save image file", e);
            }
        }
        product.setPhotos(photoUrls);
        saveProduct(product);
    }


    public ResponseEntity<ProductResponse> updatingProduct(Integer productId,
                                                             AddProductRequest updatedProductRequest,
                                                             List<MultipartFile> imageFiles) {
        try {
            Optional<Product> existingProductOptional = productRepository.findById(productId);

            if (existingProductOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Product existingProduct = existingProductOptional.get();
            existingProduct.setProduct_name(updatedProductRequest.getProductName());
            Optional<Category> categoryOptional = categoryRepository.findById(updatedProductRequest.getCategoryId());

            if (categoryOptional.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Category category = categoryOptional.get();
            existingProduct.setCategory(category);

            existingProduct.setBrand(updatedProductRequest.getBrand());
            existingProduct.setDescription(updatedProductRequest.getDescription());
            existingProduct.setStock_quantity(updatedProductRequest.getStock_quantity());
            existingProduct.setPrice(updatedProductRequest.getPrice());

            List<String> existingPhotos = existingProduct.getPhotos();
            if (existingPhotos != null && !existingPhotos.isEmpty()) {
                for (String photo : existingPhotos) {
                    String storageDir = "C:/Users/Chanmin/chatbot_project/chatbot/frontend/public/images";
                    String filePath = storageDir + photo;
                    Path path = Paths.get(filePath);
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        throw new IOException("Failed to remove image file from directory");
                    }
                }
            }

            List<String> photoImage = new ArrayList<>();

            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (MultipartFile imageFile : imageFiles) {
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
                    String uploadDir = "C:/Users/Chanmin/chatbot_project/chatbot/frontend/public/images";
                    try {
                        FileUploadUtil.saveFile(uploadDir, fileName, imageFile);
                        String photoUrl = "/images/" + fileName;
                        photoImage.add(photoUrl);
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Failed to save image file", e);
                    }
                }
            }

            existingProduct.setPhotos(photoImage);

            Product updatedProduct = productRepository.save(existingProduct);
            return ResponseEntity.ok(mapToProductResponse(updatedProduct));
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
