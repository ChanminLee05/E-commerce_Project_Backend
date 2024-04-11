package com.project.nexushub.controller;

import com.project.nexushub.dto.CartDTO;
import com.project.nexushub.dto.CartItemResponseDTO;
import com.project.nexushub.entity.Cart;
import com.project.nexushub.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/nexusHub")
@CrossOrigin(origins = "http://localhost:3000")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping("/cart")
    public ResponseEntity<List<CartDTO>> getAllProduct() {
        List<Cart> carts = shoppingCartService.getAllCart();
        if (!carts.isEmpty()) {
            List<CartDTO> cartDTOS = carts.stream()
                    .map(cart -> {
                        CartDTO cartDTO = new CartDTO();
                        cartDTO.setCartId(cart.getCartId());
                        cartDTO.setCartType(cart.getCartType());

                        List<CartItemResponseDTO> cartItemResponseDTOs = cart.getCartItems().stream()
                                .map(cartItem -> {
                                    CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
                                    cartItemResponseDTO.setCartItemId(cartItem.getCartItemId());
                                    cartItemResponseDTO.setProductId(cartItem.getProduct().getProductId());
                                    cartItemResponseDTO.setQuantity(cartItem.getQuantity());
                                    return cartItemResponseDTO;
                                })
                                .collect(Collectors.toList());

                        cartDTO.setCartItemList(cartItemResponseDTOs);
                        return cartDTO;
                    })
                    .toList();
            return ResponseEntity.ok(cartDTOS);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cart/{user_id}")
    public ResponseEntity<List<CartDTO>> getCartByUserId(@PathVariable("user_id") UUID userId) {
        List<Cart> carts = shoppingCartService.getCartByUserId(userId);
        if (!carts.isEmpty()) {
            List<CartDTO> cartDTOS = carts.stream()
                    .map(cart -> {
                        CartDTO cartDTO = new CartDTO();
                        cartDTO.setCartId(cart.getCartId());
                        cartDTO.setCartType(cart.getCartType());

                        List<CartItemResponseDTO> cartItemResponseDTOs = cart.getCartItems().stream()
                                .map(cartItem -> {
                                    CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
                                    cartItemResponseDTO.setCartItemId(cartItem.getCartItemId());
                                    cartItemResponseDTO.setProductId(cartItem.getProduct().getProductId());
                                    cartItemResponseDTO.setQuantity(cartItem.getQuantity());
                                    return cartItemResponseDTO;
                                })
                                .collect(Collectors.toList());

                        cartDTO.setCartItemList(cartItemResponseDTOs);
                        return cartDTO;
                    })
                    .toList();
            return ResponseEntity.ok(cartDTOS);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping("/add")
//    public ResponseEntity<?> addToCart(@RequestBody CartItem item, @RequestHeader("Authorization") String jwt) {
//        User currentUser = userService.getCurrentLoggedInUser(jwt);
//        if (currentUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        cartItemService.addCartItem(currentUser, item);
//        return ResponseEntity.ok().build();
//    }

}
