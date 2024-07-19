package com.cart.shoppingcart.dtos.shop.cart;

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
public class CartRequestDTO {
    private Long userId;
    private Long productId;
}
