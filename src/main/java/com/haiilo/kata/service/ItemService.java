package com.haiilo.kata.service;

import com.haiilo.kata.dto.ItemPatchDto;
import com.haiilo.kata.model.Item;
import com.haiilo.kata.repository.ItemRepository;
import com.haiilo.kata.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;

    public List<Item> findAll() {
        return this.itemRepository.findAll();
    }

    @Transactional
    public Item updatePrice(Long id, ItemPatchDto patchDto) {
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isEmpty()) {
            throw new RuntimeException("Item not found with ID: " + id);
        }

        Item item = optionalItem.get();

        // If the item had an offer, remove it as it could be now illogical with the new price
        if (item.getOffer() != null && patchDto.getPrice() != item.getPrice()) {
            item.getOffer().setItem(null);
            item.setOffer(null);
            offerRepository.deleteById(id);
        }

        item.setPrice(patchDto.getPrice());
        return itemRepository.save(item);
    }
}
