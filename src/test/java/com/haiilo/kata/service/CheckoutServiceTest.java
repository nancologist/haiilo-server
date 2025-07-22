package com.haiilo.kata.service;

import com.haiilo.kata.dto.CartItem;
import com.haiilo.kata.model.Item;
import com.haiilo.kata.model.Offer;
import com.haiilo.kata.repository.ItemRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CheckoutService checkoutService;

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

    @Test
    @DisplayName("Should calculate sum for mixed cart")
    public void shouldCalculateSumForMixedCart() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(apple));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(orange));

        CartItem appleItem = new CartItem();
        appleItem.setItemId(1L);
        appleItem.setQuantity(5);

        CartItem orangeItem = new CartItem();
        orangeItem.setItemId(2L);
        orangeItem.setQuantity(2);

        List<CartItem> cart = Arrays.asList(appleItem, orangeItem);

        double appleSum = 2 * 5.0 + 12.0;
        double orangeSum = 2 * 3;

        double expectedSum = appleSum + orangeSum;
        double actualSum = checkoutService.calculateSum(cart);

        assertEquals(expectedSum, actualSum);
        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Should calculate sum for items without offers")
    public void shouldCalculateSumForItemsWithoutOffers() {
        when(itemRepository.findById(2L)).thenReturn(Optional.of(orange));
        CartItem orange1 = new CartItem();
        orange1.setItemId(2L);
        orange1.setQuantity(2);
        List<CartItem> cart = List.of(orange1);

        double expectedSum = 2 * 3.0;
        double actualSum = checkoutService.calculateSum(cart);

        assertEquals(expectedSum, actualSum);
        verify(itemRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Should calculate sum for items with offers (remainder)")
    public void shouldCalculateSumForItemsWithOffersRemainder() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(apple));
        CartItem apple1 = new CartItem();
        apple1.setItemId(1L);
        apple1.setQuantity(5);
        List<CartItem> cart = List.of(apple1);

        double expectedSum = (1 * 12.0) + (2 * 5.0);
        double actualSum = checkoutService.calculateSum(cart);

        assertEquals(expectedSum, actualSum);
        verify(itemRepository, times(1)).findById(1L);
    }
}
