package com.project.nexushub.cartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("SELECT ci FROM CartItem ci")
    List<CartItemResponseDTO> findAllCartItem();


    Optional<CartItem> findByCart_CartIdAndCartItemId(int cartId, int cartItemId);
}
