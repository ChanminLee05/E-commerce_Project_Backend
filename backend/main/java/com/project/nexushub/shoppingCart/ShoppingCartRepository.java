package com.project.nexushub.shoppingCart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<Cart, Integer> {

    @Query("SELECT c FROM Cart c")
    List<Cart> findAllCart();

    @Query("SELECT c FROM Cart c JOIN c.user u WHERE u.user_id = :userId")
    List<Cart> findByUserId(@Param("userId") UUID userId);

}
