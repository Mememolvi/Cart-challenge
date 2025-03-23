package com.shopping.cart.challange.db.cart.item;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartItemEntry {
    private Integer netQuantity;
    private List<LocalDateTime> insertionTimestamps;
}
