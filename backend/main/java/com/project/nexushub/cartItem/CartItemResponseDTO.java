package com.project.nexushub.cartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {
    private int cartItemId;
    private String productName;
    private String brand;
    private List<String> images;
    private List<String> photos;
    private double price;
    private int quantity;

    public String getPhotoImagePath() {
        if (photos == null) return null;
        return "C:/Users/Chanmin/chatbot_project/chatbot/frontend/public/images" + photos;
    }
}
