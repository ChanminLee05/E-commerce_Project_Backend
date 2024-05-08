package com.project.nexushub.product;

import com.project.nexushub.category.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private int productId;
    private String productName;
    private String brand;
    private String description;
    private List<String> images;
    private double price;
    private int stockQuantity;
    private CategoryResponse category;
}
