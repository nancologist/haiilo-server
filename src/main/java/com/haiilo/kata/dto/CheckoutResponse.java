package com.haiilo.kata.dto;

import lombok.Getter;

@Getter
public class CheckoutResponse {
    private final double sum;

    public CheckoutResponse(double sum) {
        this.sum = sum;
    }
}
