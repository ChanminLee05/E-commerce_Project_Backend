package com.project.nexushub.shoppingCart;

import com.project.nexushub.cartItem.CartItemResponseDTO;
import com.project.nexushub.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/nexusHub")
@CrossOrigin(origins = "https://dev--nexushub-mall.netlify.app")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping("/cart/{user_id}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable("user_id") UUID userId) {
        Cart cart = shoppingCartService.getCartByUserId(userId);
        if (cart != null) {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setCartId(cart.getCartId());

            List<CartItemResponseDTO> cartItemResponseDTOs = cart.getCartItems().stream()
                    .map(cartItem -> {
                        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
                        cartItemResponseDTO.setCartItemId(cartItem.getCartItemId());

                        Product product = cartItem.getProduct();
                        cartItemResponseDTO.setProductName(product.getProduct_name());
                        cartItemResponseDTO.setBrand(product.getBrand());
                        cartItemResponseDTO.setImages(product.getImageUrl());
                        cartItemResponseDTO.setPrice(product.getPrice());
                        cartItemResponseDTO.setPhotos(product.getPhotos());
                        cartItemResponseDTO.setQuantity(cartItem.getQuantity());

                        return cartItemResponseDTO;
                    })
                    .collect(Collectors.toList());

            cartDTO.setCartItemList(cartItemResponseDTOs);
            return ResponseEntity.ok(cartDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
