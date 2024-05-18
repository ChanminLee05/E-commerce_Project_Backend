package com.project.nexushub.cartItem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.nexushub.shoppingCart.Cart;
import com.project.nexushub.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnoreProperties("cartItems")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @ElementCollection
    @CollectionTable(name = "cart_item_photos", joinColumns = @JoinColumn(name = "cart_item_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
