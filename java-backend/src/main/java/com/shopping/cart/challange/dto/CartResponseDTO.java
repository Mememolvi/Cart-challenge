package com.shopping.cart.challange.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class CartResponseDTO {
    private HashMap<String,Integer> cartItems;
    private double totalCartCost;
}
