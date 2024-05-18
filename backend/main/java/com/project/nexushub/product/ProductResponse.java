package com.project.nexushub.product;

import com.project.nexushub.category.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private List<String> imageUrl;
    private List<String> photos;
    private double price;
    private int stockQuantity;
    private CategoryResponse category;

    public String getPhotoImagePath() {
        if (photos == null) return null;
        return "C:/Users/Chanmin/chatbot_project/chatbot/frontend/public/images" + photos;
    }
}
