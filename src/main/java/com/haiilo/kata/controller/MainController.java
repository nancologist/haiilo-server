package com.haiilo.kata.controller;

import com.haiilo.kata.dto.CheckoutRequest;
import com.haiilo.kata.dto.CheckoutResponse;
import com.haiilo.kata.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final CheckoutService checkoutService;

    public MainController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkoutItems(@Valid @RequestBody CheckoutRequest request) {
        double sum = checkoutService.calculateSum(request.getCart());
        return ResponseEntity.ok(new CheckoutResponse(sum));
    }

}
