package com.trainosys.shopapi.cart;

import java.util.List;
import java.util.Map;


public interface CartItemService {
    List<CartItem> getCart(int userId);
    void addItem(int userId, CartItem newItem);
    String updateItemQuantity(int userId, int productId, int newQuantity);
    String removeItem(int userId, int productId);
    String clearCart(int userId);
    Map<Integer, List<CartItem>> getAllCarts();
    double getCartTotal(int userId);
}
