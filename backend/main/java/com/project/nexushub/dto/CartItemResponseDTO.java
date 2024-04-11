package com.project.nexushub.dto;

import com.project.nexushub.entity.CartType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {
    private Integer cartItemId;
    private Integer productId;
    private int quantity;
}
