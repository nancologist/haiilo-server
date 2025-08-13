package com.haiilo.kata.service;

import com.haiilo.kata.dto.CartItem;
import com.haiilo.kata.model.Item;
import com.haiilo.kata.model.Offer;
import com.haiilo.kata.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ItemRepository itemRepository;

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
            if (item.getOffers().length > 0) {
                itemSum = calculateItemSumWithOffer(item, quantity);
            } else {
                itemSum = item.getPrice() * quantity;
            }
            return itemSum;
        }).sum();
    }

    private double calculateItemSumWithOffer(Item item, int quantity) {
        Offer[] offer = item.getOffers();

        List<Offer> applicableDescSortedOffers =
                Arrays.stream(offer).sorted(Comparator.comparingInt(Offer::getQuantity)).filter(o -> o.getQuantity() <= quantity).toList();

        double sum = 0;
        int index = 0;
        int remained = quantity;
        Offer minOffer = applicableDescSortedOffers.getLast();

        while(remained >= applicableDescSortedOffers.get(index).getQuantity() && index < applicableDescSortedOffers.size()) {
            Offer currentOffer = applicableDescSortedOffers.get(index);
            int offerCount = quantity / currentOffer.getQuantity();
            sum += offerCount * currentOffer.getPrice();
            remained -= offerCount;
            index++;
        }

        sum += remained * item.getPrice();

        return sum;
    }
}
