package com.shopping.cart.challange.db.cart.item;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Enum representing the available shopping items in the cart system.
 * <p>
 * According to the problem statement, the number of items is limited to a specific set (Apple, Banana, Melon, Lime),
 * each with predefined pricing and optional discount rules. Using an enum ensures type safety and restricts the
 * system to only valid items, preventing errors due to invalid items. This approach also provides extensibility:
 * new items can be easily added by defining additional enum constants, and discount formulas can be modified or
 * extended by updating the associated {@link Offer} implementations without changing the core cart logic.
 * <p>
 * Each item includes a base price and an optional discount strategy (e.g., "buy one get one free" for Melon,
 * "three for two" for Lime), making it simple to adjust pricing rules as needed.
 */
@Slf4j
public enum Item {
    APPLE(35, "Apple", null),
    BANANA(20, "Banana", null),
    MELON(50, "Melon", new BuyOneGetOneFreeOffer()),
    LIME(15, "Lime", new ThreeForTwoOffer());

    private final double basePrice;
    @Getter
    private final String name;
    private final Offer offer;

    Item(double basePrice, String name, Offer offer) {
        this.basePrice = basePrice;
        this.name = name;
        this.offer = offer;
    }

    public double calculatePrice(int quantity) {
        log.info("Calculating price for {} {}", quantity, name);
        if (offer != null) {
            return offer.calculatePrice(quantity, basePrice);
        }
        double price = quantity * basePrice;
        log.info("Calculated price: {}", price);
        return BigDecimal.valueOf(price).doubleValue();
    }

    public static Item fromName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty");
        }
        String trimmedName = name.trim();
        for (Item item : Item.values()) {
            if (item.getName().equalsIgnoreCase(trimmedName)) {
                return item;
            }
        }
        throw new IllegalArgumentException("No item found with name: " + name);
    }

    private interface Offer {
        double calculatePrice(int quantity, double basePrice);
    }

    private static class BuyOneGetOneFreeOffer implements Offer {
        @Override
        public double calculatePrice(int quantity, double basePrice) {
            int setsOfTwo = quantity / 2;
            int remainder = quantity % 2;
            return (setsOfTwo + remainder) * basePrice;
        }
    }

    private static class ThreeForTwoOffer implements Offer {
        @Override
        public double calculatePrice(int quantity, double basePrice) {
            int setsOfThree = quantity / 3;
            int remainder = quantity % 3;
            return (setsOfThree * 2 + remainder) * basePrice;
        }
    }
}