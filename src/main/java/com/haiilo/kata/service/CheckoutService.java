package com.haiilo.kata.service;

import com.haiilo.kata.dto.CartItem;
import com.haiilo.kata.model.Item;
import com.haiilo.kata.model.Offer;
import com.haiilo.kata.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ItemRepository itemRepository;

    // Todo: Add some UTs for this
    public double calculateSum(List<CartItem> cart) {

        return cart.stream().mapToDouble((cartItem) -> {
            Long itemId = cartItem.getItemId();
            int quantity = cartItem.getQuantity();

            Optional<Item> optionalItem = itemRepository.findById(itemId);

            if (optionalItem.isEmpty()) {
                throw new RuntimeException("Item not found with ID: " + cartItem.getItemId());
            }

            Item item = optionalItem.get();

            double itemSum;
            if (item.getOffer() != null) {
                itemSum = calculateItemSumWithOffer(item, quantity);
            } else {
                itemSum = item.getPrice() * quantity;
            }
            return itemSum;
        }).sum();
    }

    private double calculateItemSumWithOffer(Item item, int quantity) {
        Offer offer = item.getOffer();
        int offerCount = quantity / offer.getQuantity();
        int remainCount = quantity % offer.getQuantity();

        return offerCount * offer.getPrice() + remainCount * item.getPrice();
    }
}
