package com.project.nexushub.cartItem;

import com.project.nexushub.product.FileUploadUtil;
import com.project.nexushub.shoppingCart.Cart;
import com.project.nexushub.product.Product;
import com.project.nexushub.product.ProductRepository;
import com.project.nexushub.shoppingCart.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
        Optional<CartItem> existingCartItem = cartItemRepository.findByCart_CartIdAndCartItemId(cartId, productId);

        // Product exists in cart, update quantity
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);

            cartItemRepository.save(cartItem);
        } else {
            Optional<Cart> cart = shoppingCartRepository.findById(cartId);
            Optional<Product> product = productRepository.findById(productId);

            if (cart.isPresent() && product.isPresent()) {
                Optional<List<String>> photosOptional = productRepository.findPhotosByProductId(productId);
                List<String> photos = photosOptional.orElse(Collections.emptyList());
                System.out.println("Photo" + photos);

                CartItem cartItem = new CartItem();
                cartItem.setCart(cart.get());
                cartItem.setProduct(product.get());
                cartItem.setQuantity(quantity);
                cartItem.setPhotos(photos);

                cartItemRepository.save(cartItem);
            }
        }
    }

    public void deleteProductFromCart(int cartId, int cartItemId) {
        Optional<Cart> cartOptional = shoppingCartRepository.findById(cartId);
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if (cartOptional.isPresent() && cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItemRepository.delete(cartItem);
        } else {
            throw new NoSuchElementException("CartItem not found for cartId: " + cartId + " and cartItemId: " + cartItemId);
        }
    }
}
