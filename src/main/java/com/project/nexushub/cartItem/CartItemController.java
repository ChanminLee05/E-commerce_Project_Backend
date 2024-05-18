package com.project.nexushub.cartItem;

import com.project.nexushub.authentication.JwtService;
import com.project.nexushub.shoppingCart.AddCartRequest;
import com.project.nexushub.shoppingCart.Cart;
import com.project.nexushub.shoppingCart.ShoppingCartService;
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
    private final JwtService jwtService;
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/cart-item")
    public ResponseEntity<List<CartItemResponseDTO>> getAllCartItems() {
        List<CartItemResponseDTO> cartItems = cartItemService.getAllCartItems();
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/cart-item/add")
    public ResponseEntity<String> addProductToCart(@RequestHeader(name = "Authorization") String jwtToken,
                                                   @RequestBody AddCartRequest addCartRequest
                                                   ) {

        String username = jwtService.extractUsername(jwtToken);
        System.out.println(username);

        Cart cart = shoppingCartService.getCartByUsername(username);
        System.out.println(cart);

        cartItemService.addProductToCart(cart.getCartId(), addCartRequest.getProductId(), addCartRequest.getQuantity());

        System.out.println(cart.getCartId());
        return ResponseEntity.ok("Product added to cart successfully.");
    }

    @DeleteMapping("/cart-item/delete")
    public ResponseEntity<String> deleteProductFromCart(@RequestParam int cartId, @RequestParam int cartItemId) {
        cartItemService.deleteProductFromCart(cartId, cartItemId);
        return ResponseEntity.ok("Product deleted from cart successfully");
    }

}
