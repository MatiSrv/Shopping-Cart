package com.cart.shoppingcart.controllers.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cart.shoppingcart.dtos.shop.cart.Cart;
import com.cart.shoppingcart.dtos.shop.cart.CartRequestDTO;
import com.cart.shoppingcart.services.shop.CartService;

@RestController
@RequestMapping("/cart")
@CrossOrigin("http://localhost:4200")
public class CartController {
     @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody CartRequestDTO cartRequestDTO,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(cartRequestDTO, quantity));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartByUser(id));

    }

    
    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeItemFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        CartRequestDTO cartRequestDTO = new CartRequestDTO(userId, productId);
      return ResponseEntity.ok(cartService.removeItemFromCart(cartRequestDTO));
    }

    @PutMapping("update")
    public ResponseEntity<Cart> updateQuantity(@RequestBody CartRequestDTO cart,@RequestParam Integer quantity ) {
       return ResponseEntity.ok(cartService.updateQuantityCart(cart, quantity));
    }

    @DeleteMapping("/clear/{id}")
    public ResponseEntity<Cart> clearCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.clearCart(id));
    }
}
