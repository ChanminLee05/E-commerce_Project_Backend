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
    private double price;
    private int quantity;
}
