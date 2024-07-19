package com.cart.shoppingcart.repositories.shopping;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cart.shoppingcart.entities.shopping.CartEntity;

@Repository
public interface CartJpaRepository extends JpaRepository<CartEntity, Long>{
    Optional<CartEntity> findByUserId(Long userId);
}
