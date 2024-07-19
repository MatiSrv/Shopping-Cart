package com.cart.shoppingcart.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class UserRequestDTO {
    @Email(message = "Format not valid")
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "password is required")
    private String password;
}
