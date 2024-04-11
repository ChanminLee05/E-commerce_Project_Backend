package com.project.nexushub.repository;

import com.project.nexushub.entity.Category;
import com.project.nexushub.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c")
    List<Category> findAllCategories();

    @Query("SELECT c FROM Category c WHERE c.categoryId = :categoryId")
    Optional<Category> findProductByCategoryId(Integer categoryId);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.categoryId = :categoryId")
    boolean existsByCategoryId(@Param("categoryId") Integer categoryId);
}
