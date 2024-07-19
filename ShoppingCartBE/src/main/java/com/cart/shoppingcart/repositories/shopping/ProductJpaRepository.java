package com.cart.shoppingcart.repositories.shopping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cart.shoppingcart.entities.shopping.ProductEntity;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long>{
}
