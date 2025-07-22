package com.haiilo.kata.service;

import com.haiilo.kata.dto.CartItem;
import com.haiilo.kata.model.Item;
import com.haiilo.kata.model.Offer;
import com.haiilo.kata.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CheckoutService {

    private final ItemRepository itemRepository;

    public CheckoutService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Todo: Add some UTs for this
    public double calculateSum(List<CartItem> cart) {

        return cart.stream().mapToDouble((cartItem) -> {
            Long itemId = cartItem.getItemId();
            int quantity = cartItem.getQuantity();

            Optional<Item> item = itemRepository.findById(itemId);

            if (item.isPresent()) {
                double itemSum;
                if (item.get().getOffer() != null) {
                    itemSum = calculateItemSumWithOffer(item.get(), quantity);
                } else {
                    itemSum = item.get().getPrice() * quantity;
                }
                return itemSum;
            }
            return 0;
        }).sum();
    }

    private double calculateItemSumWithOffer(Item item, int quantity) {
        Offer offer = item.getOffer();
        int offerCount = quantity / offer.getQuantity();
        int remainCount = quantity % offer.getQuantity();

        return offerCount * offer.getPrice() + remainCount * item.getPrice();
    }
}
