package com.cart.shoppingcart.dtos.shop.cart;

import java.util.List;

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
public class Cart {
    public Long id;
    public Long userId;
    public List<CartItem> items;
}
