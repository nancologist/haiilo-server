package com.haiilo.kata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPatchDto {

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price should be greater than zero")
    private double price;
}
