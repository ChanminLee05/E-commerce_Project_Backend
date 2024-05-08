package com.project.nexushub.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {
    private String productName;
    private int categoryId;
    private String brand;
    private String description;
    private int stock_quantity;
    private double price;
    private List<String> imageUrl;
}
