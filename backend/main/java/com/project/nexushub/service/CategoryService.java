package com.project.nexushub.service;

import com.project.nexushub.dto.AddCategoryRequest;
import com.project.nexushub.dto.CategoryResponse;
import com.project.nexushub.entity.Category;
import com.project.nexushub.entity.Product;
import com.project.nexushub.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategories() {

        List<Category> categories = categoryRepository.findAllCategories();
        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .categoryId(category.getCategoryId())
                        .categoryName(category.getCategory_name())
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<CategoryResponse> getCategoryByCategoryId(int categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findProductByCategoryId(categoryId);
        return categoryOptional.map(this::mapToCategoryResponse);
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategory_name())
                .build();
    }

    public CategoryResponse addCategory(AddCategoryRequest addCategoryRequest) {

        Category category = Category.builder()
                .category_name(addCategoryRequest.getTitle())
                .build();

        Category addedCategory = categoryRepository.save(category);
        return CategoryResponse.builder()
                .categoryId(addedCategory.getCategoryId())
                .categoryName(addedCategory.getCategory_name())
                .build();
    }

    @PostConstruct
    public void initializeCategories() {
        if (categoryRepository.count() == 0) {
            Category fashionCategory = new Category();
            fashionCategory.setCategoryId(1);
            fashionCategory.setCategory_name("Fashion & Clothes");
            categoryRepository.save(fashionCategory);

            Category electronicsCategory = new Category();
            electronicsCategory.setCategoryId(2);
            electronicsCategory.setCategory_name("Electronics");
            categoryRepository.save(electronicsCategory);

            Category homeGardenCategory = new Category();
            homeGardenCategory.setCategoryId(3);
            homeGardenCategory.setCategory_name("Home & Garden");
            categoryRepository.save(homeGardenCategory);

            Category miscellaneousCategory = new Category();
            miscellaneousCategory.setCategoryId(4);
            miscellaneousCategory.setCategory_name("Miscellaneous");
            categoryRepository.save(miscellaneousCategory);
        }
    }
}
