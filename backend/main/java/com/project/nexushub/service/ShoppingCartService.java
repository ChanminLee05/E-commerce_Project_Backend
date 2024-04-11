package com.project.nexushub.service;

import com.project.nexushub.entity.*;
import com.project.nexushub.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public List<Cart> getAllCart() {
        return shoppingCartRepository.findAllCart();
    }

    public List<Cart> getCartByUserId(UUID userId) {
        return shoppingCartRepository.findByUserId(userId);
    }

}
