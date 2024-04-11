package com.project.nexushub.dto;

import com.project.nexushub.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


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
