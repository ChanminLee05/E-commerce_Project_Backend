package com.project.nexushub.dto;

import com.project.nexushub.entity.CartItem;
import com.project.nexushub.entity.CartType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Integer cartId;
    private CartType cartType;
    private List<CartItemResponseDTO> cartItemList;
}
