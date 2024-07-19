package com.cart.shoppingcart.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cart.shoppingcart.dtos.shop.ProductDTO;
import com.cart.shoppingcart.dtos.shop.cart.Cart;
import com.cart.shoppingcart.dtos.shop.cart.CartItem;
import com.cart.shoppingcart.dtos.shop.cart.CartRequestDTO;
import com.cart.shoppingcart.entities.shopping.CartEntity;
import com.cart.shoppingcart.entities.shopping.CartItemEntity;
import com.cart.shoppingcart.entities.shopping.ProductEntity;
import com.cart.shoppingcart.entities.user.UserEntity;
import com.cart.shoppingcart.repositories.shopping.CartItemJpaRepository;
import com.cart.shoppingcart.repositories.shopping.CartJpaRepository;
import com.cart.shoppingcart.repositories.shopping.ProductJpaRepository;
import com.cart.shoppingcart.repositories.user.UserJpaRepository;
import com.cart.shoppingcart.services.shop.CartService;

import jakarta.persistence.EntityNotFoundException;

public class CartServiceTest {

        @Mock
        private CartJpaRepository cartJpaRepository;

        @Mock
        private CartItemJpaRepository cartItemJpaRepository;

        @Mock
        private UserJpaRepository userJpaRepository;

        @Mock
        private ProductJpaRepository productJpaRepository;

        @InjectMocks
        private CartService cartService;

        private UserEntity user;
        private ProductEntity product;
        private CartEntity cart;
        private CartItemEntity cartItem;

        @BeforeEach
        void setUp() {
                user = new UserEntity();
                user.setId(1L);
                user.setEmail("Test@User");

                product = new ProductEntity();
                product.setId(1L);
                product.setName("Test Product");
                product.setPrice(BigDecimal.valueOf(100.0));

                cart = new CartEntity();
                cart.setId(1L);
                cart.setUser(user);
                cart.setItems(new ArrayList<>());

                cartItem = new CartItemEntity();
                cartItem.setId(1L);
                cartItem.setProduct(product);
                cartItem.setCart(cart);
                cartItem.setQuantity(2);
                cart.getItems().add(cartItem);

                MockitoAnnotations.openMocks(this);

        }

        @Test
        public void addItemToCartTest_UserOrProductNotFound() {
                when(userJpaRepository.findById(anyLong())).thenReturn(Optional.empty());
                when(productJpaRepository.findById(anyLong())).thenReturn(Optional.of(product));

                CartRequestDTO cartRequestDTO = new CartRequestDTO();
                cartRequestDTO.setUserId(1L);
                cartRequestDTO.setProductId(1L);

                RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                        cartService.addItemToCart(cartRequestDTO, 1);
                });

                assertEquals("User or Product not found", exception.getMessage());
        }

        @Test
        public void addItemToCartTest() {
                when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(productJpaRepository.findById(anyLong())).thenReturn(Optional.of(product));
                when(cartJpaRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
                when(cartJpaRepository.save(any(CartEntity.class))).thenReturn(cart);
                when(cartItemJpaRepository.save(any(CartItemEntity.class))).thenReturn(cartItem);

                CartRequestDTO cartRequestDTO = new CartRequestDTO();
                cartRequestDTO.setUserId(1L);
                cartRequestDTO.setProductId(1L);

                Cart result = cartService.addItemToCart(cartRequestDTO, 1);
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(1L);
                productDTO.setName("Test Product");
                productDTO.setPrice(BigDecimal.valueOf(100.0));
                Cart expectedCart = new Cart(1L, 1L, List.of(new CartItem(1L, productDTO, 2)));

                assertEquals(expectedCart.getId(), result.getId());
                assertEquals(1, result.getUserId());
                assertEquals(1, result.getItems().size());
                assertEquals(expectedCart.getItems().get(0).product.getId(),
                                expectedCart.getItems().get(0).product.getId());
        }

        @Test
        public void getCartByUserTest() {
                when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(user));
                when(cartJpaRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
                when(cartJpaRepository.save(any(CartEntity.class))).thenReturn(cart);

                Cart result = cartService.getCartByUser(1L);
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(1L);
                productDTO.setName("Test Product");
                productDTO.setPrice(BigDecimal.valueOf(100.0));
                Cart expectedCart = new Cart(1L, 1L, List.of(new CartItem(1L, productDTO, 2)));

                assertEquals(expectedCart.getItems().size(), expectedCart.getItems().size());
                assertEquals(expectedCart.getId(), result.getId());
                assertEquals(1, result.getUserId());

        }

        @Test
        public void updateQuantityCartTest() {
                when(cartJpaRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
                when(cartItemJpaRepository.save(any(CartItemEntity.class))).thenReturn(cartItem);
                when(cartJpaRepository.save(any(CartEntity.class))).thenReturn(cart);

                CartRequestDTO cartRequestDTO = new CartRequestDTO();
                cartRequestDTO.setUserId(1L);
                cartRequestDTO.setProductId(1L);

                Cart result = cartService.updateQuantityCart(cartRequestDTO, 3);

                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(1L);
                productDTO.setName("Test Product");
                productDTO.setPrice(BigDecimal.valueOf(100.0));
                Cart expectedCart = new Cart(1L, 1L, List.of(new CartItem(1L, productDTO, 3)));

                assertEquals(expectedCart.getItems().get(0).quantity, result.getItems().get(0).quantity);
        }

        @Test
        public void updateQuantityCartTest_CartNotFound() {
                when(cartJpaRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

                CartRequestDTO cartRequestDTO = new CartRequestDTO();
                cartRequestDTO.setUserId(1L);
                cartRequestDTO.setProductId(1L);

                EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
                        cartService.updateQuantityCart(cartRequestDTO, 3);
                });

                assertEquals("Cart not found", exception.getMessage());
        }

        @Test
        public void testRemoveItemFromCart() throws Exception {

                CartEntity existingCart = cart;

                when(cartJpaRepository.findByUserId(anyLong())).thenReturn(Optional.of(existingCart));
                when(cartJpaRepository.save(any(CartEntity.class))).thenReturn(cart);

                // Act
                Cart removedItemCart = cartService.removeItemFromCart(new CartRequestDTO(1L, 1L));
                
                verify(cartItemJpaRepository, times(1)).delete(any(CartItemEntity.class));
                verify(cartJpaRepository, times(1)).save(any(CartEntity.class));
                assertTrue(removedItemCart.getItems().isEmpty());

        }


        @Test
        public void testRemoveClearCart() throws Exception {

                CartEntity existingCart = cart;

                when(cartJpaRepository.findByUserId(anyLong())).thenReturn(Optional.of(existingCart));
                when(cartJpaRepository.save(any(CartEntity.class))).thenReturn(cart);

                // Act
                Cart removedItemCart = cartService.clearCart(1L);
                
                verify(cartItemJpaRepository, times(1)).deleteAllByCartId(anyLong());
                verify(cartJpaRepository, times(1)).save(any(CartEntity.class));
                assertTrue(removedItemCart.getItems().isEmpty());

        }

}
