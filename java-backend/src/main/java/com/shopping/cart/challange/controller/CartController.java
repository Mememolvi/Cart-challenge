package com.shopping.cart.challange.controller;

import com.shopping.cart.challange.dto.AddItemDTO;
import com.shopping.cart.challange.dto.CartResponseDTO;
import com.shopping.cart.challange.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;


    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Adds an item to the cart.
     *
     * @param addItemDTO the AddItemDTO containing the item name
     * @return ResponseEntity with a success or error message
     */
    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestBody AddItemDTO addItemDTO) {
        try {
            if (addItemDTO == null) {
                return ResponseEntity.badRequest().body("Pass a Valid add Item Payload");
            }
            if (addItemDTO.getItemName() == null || addItemDTO.getItemName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Item name cannot be null or empty");
            }
            String result = cartService.addItem(addItemDTO.getItemName());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid item: '" + addItemDTO.getItemName() + "' is not a valid item.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error adding item: " + e.getMessage());
        }
    }

    /**
     * Retrieves all items in the cart with their quantities and total amount.
     *
     * @return ResponseEntity containing the CartResponseDTO
     */
    @GetMapping("/getAllItems")
    public ResponseEntity<CartResponseDTO> getCartItems() {
        try {
            CartResponseDTO cartResponse = cartService.getAllItems();
            return ResponseEntity.ok(cartResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Fetches the total cost of the cart.
     *
     * @return ResponseEntity with the total cost
     */
    @GetMapping("/getTotal")
    public ResponseEntity<Double> getTotalCost() {
        try {
            double totalCost = cartService.fetchTotalCost();
            return ResponseEntity.ok(totalCost);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}