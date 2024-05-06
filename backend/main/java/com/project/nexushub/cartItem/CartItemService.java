package com.project.nexushub.cartItem;

import com.project.nexushub.shoppingCart.Cart;
import com.project.nexushub.product.Product;
import com.project.nexushub.product.ProductRepository;
import com.project.nexushub.shoppingCart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;

    public CartItemService(CartItemRepository cartItemRepository, ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
    }

    public List<CartItemResponseDTO> getAllCartItems() {
        return cartItemRepository.findAllCartItem();
    }
    public void addProductToCart(int cartId, int productId, int quantity) {
        Optional<CartItem> existingCartItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(cartId, productId);

        // Product exists in cart, update quantity
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);

            cartItemRepository.save(cartItem);
        } else {
            Optional<Cart> cart = shoppingCartRepository.findById(cartId);
            Optional<Product> product = productRepository.findById(productId);

            if (cart.isPresent() && product.isPresent()) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart.get());
                cartItem.setProduct(product.get());
                cartItem.setQuantity(quantity);

                cartItemRepository.save(cartItem);
            }
        }
    }

    public void deleteProductFromCart(int cartId, int productId) {
        Optional<CartItem> existingCartItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(cartId, productId);

        // Product exists in cart, update quantity
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItemRepository.delete(cartItem);
        } else {
            // Handle the case where the CartItem does not exist
            throw new NoSuchElementException("CartItem not found for cartId: " + cartId + " and productId: " + productId);
        }
    }
}
