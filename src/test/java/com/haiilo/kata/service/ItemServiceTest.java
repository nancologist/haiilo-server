package com.haiilo.kata.service;

import com.haiilo.kata.dto.ItemPatchDto;
import com.haiilo.kata.model.Item;
import com.haiilo.kata.model.Offer;
import com.haiilo.kata.repository.ItemRepository;
import com.haiilo.kata.repository.OfferRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private ItemService itemService;

    private Item apple;
    private Item orange;

    @BeforeEach
    public void setUp() {
        apple = new Item();
        apple.setId(1L);
        apple.setName("Apple");
        apple.setPrice(5.0);

        Offer appleOffer = new Offer();
        appleOffer.setItemId(1L);
        appleOffer.setQuantity(3);
        appleOffer.setPrice(12.0);
        apple.setOffer(appleOffer);

        orange = new Item();
        orange.setId(2L);
        orange.setName("Orange");
        orange.setPrice(3.0);
        orange.setOffer(null);
    }

    @DisplayName("Should update price of an item which doesn't have an offer")
    @Test
    public void shouldUpdatePricePriceWithoutOffer() {
        when(itemRepository.findById(2L)).thenReturn(Optional.of(orange));
        when(itemRepository.save(orange)).thenReturn(orange);

        ItemPatchDto patchDto = new ItemPatchDto();
        patchDto.setPrice(4.0);

        Item updatedItem = itemService.updatePrice(2L, patchDto);

        Assertions.assertEquals(4.0, updatedItem.getPrice());
        verify(itemRepository, times(1)).findById(2L);
        verify(offerRepository, never()).deleteById(2L);
    }

    @DisplayName("Should update price of an item which has an offer")
    @Test
    public void shouldUpdatePricePriceWithOffer() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(apple));
        when(itemRepository.save(apple)).thenReturn(apple);

        ItemPatchDto patchDto = new ItemPatchDto();
        patchDto.setPrice(6.0);

        Item updatedItem = itemService.updatePrice(1L, patchDto);

        Assertions.assertEquals(6.0, updatedItem.getPrice());
        Assertions.assertNull(updatedItem.getOffer());
        verify(itemRepository, times(1)).findById(1L);
        verify(offerRepository, times(1)).deleteById(1L);
    }

    @DisplayName("Should not delete the offer if price does not change")
    @Test
    public void shouldNotDeleteOfferIfPriceDoesNotChange() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(apple));
        when(itemRepository.save(apple)).thenReturn(apple);

        ItemPatchDto patchDto = new ItemPatchDto();
        patchDto.setPrice(apple.getPrice());

        Item updatedItem = itemService.updatePrice(1L, patchDto);

        Assertions.assertNotNull(updatedItem.getOffer());
        verify(itemRepository, times(1)).findById(1L);
        verify(offerRepository, never()).deleteById(1L);
    }

    @DisplayName("Should find all items")
    @Test
    public void shouldFindAllItems() {
        when(itemRepository.findAll()).thenReturn(Arrays.asList(orange, apple));
        List<Item> items = itemService.findAll();
        Assertions.assertEquals(2, items.size());
        verify(itemRepository, times(1)).findAll();
    }
}
