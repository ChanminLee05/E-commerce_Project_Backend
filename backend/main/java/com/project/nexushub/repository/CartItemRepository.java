package com.project.nexushub.repository;

import com.project.nexushub.dto.CartItemResponseDTO;
import com.project.nexushub.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("SELECT new com.project.nexushub.dto.CartItemResponseDTO(ci.cartItemId, ci.product.productId, ci.quantity) FROM CartItem ci")
    List<CartItemResponseDTO> findAllCartItem();


    Optional<CartItem> findByCart_CartIdAndProduct_ProductId(int cartId, int productId);
}
