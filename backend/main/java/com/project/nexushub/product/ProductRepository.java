package com.project.nexushub.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p")
    List<Product> findAllProducts();

    @Query("SELECT p FROM Product p WHERE p.productId = :productId")
    Optional<Product> findProductByProductId(Integer productId);

    @Query("SELECT p FROM Product p WHERE p.product_name = :productName")
    Optional<Product> findProductByProductName(String productName);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    List<Product> findAllbyCategoryId(Integer categoryId);

    @Query("SELECT p.photos FROM Product p WHERE p.productId = :productId")
    Optional<List<String>> findPhotosByProductId(Integer productId);
}
