package com.project.nexushub.cartItem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/nexusHub")
@CrossOrigin(origins = "http://localhost:3000")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;

    @GetMapping("/cart-item")
    public ResponseEntity<List<CartItemResponseDTO>> getAllCartItems() {
        List<CartItemResponseDTO> cartItems = cartItemService.getAllCartItems();
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/cart-item/add")
    public ResponseEntity<String> addProductToCart(@RequestParam int cartId,
                                                   @RequestParam int productId,
                                                   @RequestParam int quantity
                                                   ) {
        cartItemService.addProductToCart(cartId, productId, quantity);
        return ResponseEntity.ok("Product added to cart successfully.");
    }

    @DeleteMapping("/cart-item/delete")
    public ResponseEntity<String> deleteProductFromCart(@RequestParam int cartId, @RequestParam int productId) {
        cartItemService.deleteProductFromCart(cartId, productId);
        return ResponseEntity.ok("Product deleted from cart successfully");
    }

}
