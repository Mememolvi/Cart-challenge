package com.shopping.cart.challange.service.impl;

import com.shopping.cart.challange.db.CartRepository;
import com.shopping.cart.challange.db.cart.item.CartItemEntry;
import com.shopping.cart.challange.db.cart.item.Item;
import com.shopping.cart.challange.dto.CartResponseDTO;
import com.shopping.cart.challange.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public String addItem(String item) {
        cartRepository.addItem(Item.fromName(item));
        return item;
    }

    @Override
    public CartResponseDTO getAllItems() {
        Map<Item, CartItemEntry> items = cartRepository.getAllItems();
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        mapItemsToCartResponseDTO(items, cartResponseDTO);
        return cartResponseDTO;
    }

    @Override
    public double fetchTotalCost() {
        return cartRepository.calculateTotal();
    }

    private void mapItemsToCartResponseDTO(Map<Item, CartItemEntry> items, CartResponseDTO cartResponseDTO) {
        HashMap<String, Integer> cartItems = new HashMap<>();

        for (Map.Entry<Item, CartItemEntry> entry : items.entrySet()) {
            cartItems.put(entry.getKey().name(), entry.getValue().getNetQuantity());
        }

        cartResponseDTO.setCartItems(cartItems);
        cartResponseDTO.setTotalCartCost(cartRepository.calculateTotal());
    }
}
