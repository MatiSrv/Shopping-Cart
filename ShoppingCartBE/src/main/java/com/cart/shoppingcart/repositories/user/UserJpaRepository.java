package com.cart.shoppingcart.repositories.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cart.shoppingcart.entities.user.UserEntity;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity,Long> {
        
      Optional<UserEntity> findByEmail(String email);
}
