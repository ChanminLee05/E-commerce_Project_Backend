package com.project.nexushub.product;

import com.project.nexushub.cartItem.CartItem;
import com.project.nexushub.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private int productId;

    private String product_name;
    private String description;
//    private byte[] image;
    private double price;
    private int stock_quantity;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private Set<CartItem> cartItems;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
