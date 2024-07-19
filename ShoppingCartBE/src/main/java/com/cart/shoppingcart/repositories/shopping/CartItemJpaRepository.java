package com.cart.shoppingcart.repositories.shopping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cart.shoppingcart.entities.shopping.CartItemEntity;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemEntity,Long> {
    void deleteAllByCartId(Long id);
}
