package com.trainosys.shopapi.cart;

import java.util.List;

public interface CartItemService {
    List<CartItem> getCart(Long userId);
    void addItem(Long userId, CartItem newItem);
    String updateItemQuantity(Long userId, Long productId, int newQuantity);
    String removeItem(Long userId, Long productId);
    String clearCart(Long userId);
    List<CartItem> getAllCartItems(); // Flattened since data comes from a DB now
    double getCartTotal(Long userId);
}
