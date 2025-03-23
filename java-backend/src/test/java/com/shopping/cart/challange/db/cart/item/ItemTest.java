package com.shopping.cart.challange.db.cart.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItemTest {

    @Test
    public void testApplePriceCalculation() {
        Item apple = Item.APPLE;
        assertEquals(0, apple.calculatePrice(0));
        assertEquals(35, apple.calculatePrice(1));
        assertEquals(70, apple.calculatePrice(2));
        assertEquals(105, apple.calculatePrice(3));
    }

    @Test
    public void testBananaPriceCalculation() {
        Item banana = Item.BANANA;
        assertEquals(0, banana.calculatePrice(0));
        assertEquals(20, banana.calculatePrice(1));
        assertEquals(40, banana.calculatePrice(2));
        assertEquals(60, banana.calculatePrice(3));
    }

    @Test
    public void testMelonPriceCalculation() {
        Item melon = Item.MELON;
        assertEquals(0, melon.calculatePrice(0));
        assertEquals(50, melon.calculatePrice(1));
        assertEquals(50, melon.calculatePrice(2));
        assertEquals(100, melon.calculatePrice(3));
        assertEquals(100, melon.calculatePrice(4));
        assertEquals(150, melon.calculatePrice(5));
    }

    // Test Three For Two offer for LIME
    @Test
    public void testLimePriceCalculation() {
        Item lime = Item.LIME;
        assertEquals(0, lime.calculatePrice(0));
        assertEquals(15, lime.calculatePrice(1));
        assertEquals(30, lime.calculatePrice(2));
        assertEquals(30, lime.calculatePrice(3));
        assertEquals(45, lime.calculatePrice(4));
        assertEquals(60, lime.calculatePrice(5));
        assertEquals(60, lime.calculatePrice(6));
    }

    @Test
    public void testFromNameValid() {
        assertEquals(Item.APPLE, Item.fromName("Apple"));
        assertEquals(Item.BANANA, Item.fromName("banana"));
        assertEquals(Item.MELON, Item.fromName("MELON"));
        assertEquals(Item.LIME, Item.fromName("lime "));
    }

    @Test
    public void testFromNameInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Item.fromName(null));
        assertThrows(IllegalArgumentException.class, () -> Item.fromName(""));
        assertThrows(IllegalArgumentException.class, () -> Item.fromName("Orange"));
    }
}