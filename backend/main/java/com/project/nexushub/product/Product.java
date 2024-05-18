package com.project.nexushub.product;

import com.project.nexushub.cartItem.CartItem;
import com.project.nexushub.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "product_name", nullable = false)
    private String product_name;
    private String brand;
    @Column(length = 500)
    private String description;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> imageUrl;

    @ElementCollection
    @CollectionTable(name = "product_photos", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    public String getPhotoImagePath() {
        if (photos == null) return null;
        return "C:/Users/Chanmin/chatbot_project/chatbot/frontend/public/images" + photos;
    }

    private double price;
    private int stock_quantity;

    @OneToMany(mappedBy = "product")
    private Set<CartItem> cartItems;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
