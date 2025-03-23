package com.shopping.cart.challange.service;

import com.shopping.cart.challange.dto.CartResponseDTO;

/**
 * Interface defining the core operations for managing a shopping cart.
 * Implementations should handle adding items, retrieving cart contents,
 * and calculating totals, with support for timestamped insertions.
 */
public interface CartService {
    /**
     * Adds an item to the cart with the current timestamp.
     *
     * @param item the Item to add
     * @return a message indicating the result of the operation
     */
    String addItem(String item);

    /**
     * Retrieves all items currently in the cart.
     *
     * @return a Map of Item to CartResponseDTO containing cart contents
     */
    CartResponseDTO getAllItems();

    /**
     * Calculates the total cost of all items in the cart, applying any pricing rules.
     *
     * @return the total price in pounds
     */
    double fetchTotalCost();

}