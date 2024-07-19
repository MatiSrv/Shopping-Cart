package com.cart.shoppingcart.services.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import jakarta.persistence.EntityNotFoundException;

@Service
public class CartService {
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ProductJpaRepository productJpaRepository;
    @Autowired
    private CartJpaRepository cartJpaRepository;
    @Autowired
    private CartItemJpaRepository cartItemJpaRepository;


    public Cart addItemToCart(CartRequestDTO cartRequestDTO, Integer quantity) {
    Optional<UserEntity> userOptional = userJpaRepository.findById(cartRequestDTO.getUserId());
    Optional<ProductEntity> productOptional = productJpaRepository.findById(cartRequestDTO.getProductId());

    if (userOptional.isEmpty() || productOptional.isEmpty()) {
        throw new RuntimeException("User or Product not found");
    }

    UserEntity user = userOptional.get();
    ProductEntity product = productOptional.get();

    CartEntity cart = cartJpaRepository.findByUserId(cartRequestDTO.getUserId()).orElseGet(() -> {
        CartEntity newCart = new CartEntity();
        newCart.setUser(user);
        newCart.setItems(new ArrayList<>());
        return newCart;
    });

    // Guardar el carrito si es nuevo
    if (cart.getId() == null) {
        cartJpaRepository.save(cart);
    }

    CartItemEntity item = cart.getItems()
            .stream()
            .filter(itemCart -> itemCart.getProduct().getId().equals(cartRequestDTO.getProductId()))
            .findFirst()
            .orElseGet(() -> {
                CartItemEntity newItem = new CartItemEntity();
                newItem.setCart(cart);
                newItem.setQuantity(0); // Inicializa la cantidad en 0
                return newItem;
            });

    item.setProduct(product);
    item.setQuantity(item.getQuantity() + quantity);

    cartItemJpaRepository.save(item);

    if (!cart.getItems().contains(item)) {
        cart.getItems().add(item);
    }
   CartEntity saved =  cartJpaRepository.save(cart);

    return getCart(saved);
}

public Cart getCartByUser(Long id) {
    UserEntity user = userJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

    CartEntity cart = cartJpaRepository.findByUserId(id).orElseGet(() -> {
        CartEntity newCart = new CartEntity();
        newCart.setUser(user);
        newCart.setItems(new ArrayList<>());
        return newCart;
    });
     return getCart(cartJpaRepository.save(cart));

  
}

 

    public Cart removeItemFromCart(CartRequestDTO cartRequestDTO) {
       CartEntity cart = getCartByUserId(cartRequestDTO.getUserId());
        if (cart != null) {
            CartItemEntity cartItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(cartRequestDTO.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                cart.getItems().remove(cartItem);
                cartItemJpaRepository.delete(cartItem);
               cart = cartJpaRepository.save(cart);
            }
        } else{
            throw new EntityNotFoundException("Cart not found");
        }
        return getCart(cart);
    }

    
    public Cart updateQuantityCart(CartRequestDTO cart, Integer quantity) {
        CartEntity cartEntity = getCartByUserId(cart.getUserId());
        CartEntity saved = null;
        if (cartEntity != null) {
            CartItemEntity cartItem = cartEntity.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(cart.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                cartItem.setQuantity(quantity);
                cartItemJpaRepository.save(cartItem);
                saved = cartJpaRepository.save(cartEntity);
            }
        } else{
            throw new EntityNotFoundException("Cart not found");
        }
        return getCart(saved);
    }

    @Transactional
    public Cart clearCart(Long id){
        CartEntity saved = new CartEntity();
        CartEntity cart = getCartByUserId(id);
        if (cart != null) {
            cart.getItems().clear();
            cartItemJpaRepository.deleteAllByCartId(cart.getId());
           saved =cartJpaRepository.save(cart);
        }else {
            throw new EntityNotFoundException("Cart not found");
        }

        return getCart(saved);
    }

    public CartEntity getCartByUserId(Long id){
        return cartJpaRepository.findByUserId(id).orElse(null);
    }

    private Cart getCart(CartEntity cart){
        List<CartItem> items = cart.getItems()
                .stream().map(itemCartS -> CartItem.builder()
                        .id(itemCartS.getId()) 
                        .product(getCartProductDTO(itemCartS.getProduct()))
                        .quantity(itemCartS.getQuantity()).build())
                .toList();

        return Cart.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .build();
    }

    private ProductDTO getCartProductDTO(ProductEntity product) {
       
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
