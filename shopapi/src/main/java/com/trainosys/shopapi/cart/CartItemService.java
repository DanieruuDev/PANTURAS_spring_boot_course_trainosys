package com.trainosys.shopapi.cart;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartItemService {
    private final Map<Integer, List<CartItem>> cartStorage = new ConcurrentHashMap<>();


    public List<CartItem> getCart(int userId) {
        return cartStorage.getOrDefault(userId, new ArrayList<>());
    }

    public void addItem(int userId, CartItem newItem) {
        List<CartItem> cart = cartStorage.computeIfAbsent(userId, k -> new ArrayList<>());

        for (CartItem item : cart) {
            if (item.getProductId() == newItem.getProductId()) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        cart.add(newItem);
    }

    public void updateItemQuantity(int userId, int productId, int newQuantity) {
        List<CartItem> cart = cartStorage.get(userId);
        if (cart != null) {
            for (CartItem item : cart) {
                if (item.getProductId() == productId) {
                    item.setQuantity(newQuantity);
                    return;
                }
            }
        }
    }

    public void removeItem(int userId, int productId) {
        List<CartItem> cart = cartStorage.get(userId);
        if (cart != null) {
            cart.removeIf(item -> item.getProductId() == productId);
        }
    }

    public void clearCart(int userId) {
        cartStorage.remove(userId);
    }
}
