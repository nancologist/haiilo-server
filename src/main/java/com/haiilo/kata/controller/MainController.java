package com.haiilo.kata.controller;

import com.haiilo.kata.dto.CheckoutRequest;
import com.haiilo.kata.dto.CheckoutResponse;
import com.haiilo.kata.dto.ItemPatchDto;
import com.haiilo.kata.model.Item;
import com.haiilo.kata.service.CheckoutService;
import com.haiilo.kata.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Todo: consider creating dedicated controllers for each domain if amount of endpoints grow.
@RestController
@RequiredArgsConstructor
public class MainController {

    private final CheckoutService checkoutService;
    private final ItemService itemService;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkoutItems(@Valid @RequestBody CheckoutRequest request) {
        double sum = checkoutService.calculateSum(request.getCart());
        return ResponseEntity.ok(new CheckoutResponse(sum));
    }

    @PatchMapping("/item/{id}")
    public ResponseEntity<Item> patchItem(@PathVariable Long id, @Valid @RequestBody ItemPatchDto patchDto) {
        Item updatedItem = this.itemService.updatePrice(id, patchDto);
        return ResponseEntity.ok(updatedItem);
    }
}
