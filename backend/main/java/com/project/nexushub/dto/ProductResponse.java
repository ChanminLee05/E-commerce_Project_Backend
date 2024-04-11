package com.project.nexushub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private int productId;
    private String productName;
    private String description;
    private String image;
    private LocalDate orderDate;
    private double price;
    private int stockQuantity;
    private CategoryResponse category;
}
