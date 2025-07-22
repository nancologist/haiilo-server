package com.haiilo.kata.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutRequest {

    @NotNull(message = "Cart cannot be null")
    @NotEmpty(message = "Cart cannot be empty")
    private List<CartItem> cart;
}
