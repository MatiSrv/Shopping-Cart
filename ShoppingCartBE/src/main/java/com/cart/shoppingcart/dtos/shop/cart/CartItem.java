package com.cart.shoppingcart.dtos.shop.cart;

import com.cart.shoppingcart.dtos.shop.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    public Long id;
    public ProductDTO product;
    public Integer quantity;
}
