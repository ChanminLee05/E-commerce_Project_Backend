package com.project.nexushub.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {
    private String productName;
    private int categoryId;
    private String description;
    private int stock_quantity;
    private double price;


//    private MultipartFile image;
}
