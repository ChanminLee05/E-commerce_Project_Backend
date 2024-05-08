package com.project.nexushub.shoppingCart;

import com.project.nexushub.cartItem.CartItemResponseDTO;
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
    private List<CartItemResponseDTO> cartItemList;
}
