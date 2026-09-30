package com.trainosys.shopapi.cart;

import com.trainosys.shopapi.product.Product;
import com.trainosys.shopapi.product.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartServiceImpl implements CartItemService {
    private final Map<Integer, List<CartItem>> cartStorage = new ConcurrentHashMap<>();

    private final ProductServiceImpl productService;
    public CartServiceImpl (ProductServiceImpl productService){
        this.productService = productService;
    }

    public List<CartItem> getCart(int userId) {
        return cartStorage.getOrDefault(userId, new ArrayList<>());
    }

    public void addItem(int userId, CartItem newItem) {
        if (newItem == null || newItem.getQuantity() <= 0 || newItem.getProductId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: Product details or quantity are incorrect");
        }
        List<CartItem> cart = cartStorage.computeIfAbsent(userId, k -> new ArrayList<>());
        for (CartItem item : cart) {
            if (item.getProductId() == newItem.getProductId()) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        cart.add(newItem);
    }

    public String updateItemQuantity(int userId, int productId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: Quantity must be greater than zero");
        }
        List<CartItem> cart = cartStorage.get(userId);
        if (cart != null) {
            for (CartItem item : cart) {
                if (item.getProductId() == productId) {
                    item.setQuantity(newQuantity);
                    return "Quantity updated successfully";
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product ID " + productId + " not found in user's cart.");
    }

    public String removeItem(int userId, int productId) {
        List<CartItem> cart = cartStorage.get(userId);
        if (cart != null) {
            boolean removed = cart.removeIf(item -> item.getProductId() == productId);
            if (removed) {
                return "Product ID " + productId + " removed from User " + userId + "'s cart.";
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product ID " + productId + " not found in user's cart.");
    }

    public String clearCart(int userId) {
        if (cartStorage.containsKey(userId)) {
            cartStorage.remove(userId);
            return "Cart for User " + userId + " has been successfully cleared.";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for User ID: " + userId);
    }

    public Map<Integer, List<CartItem>> getAllCarts() {
        return cartStorage;
    }

    public double getCartTotal(int userId) {
        List<CartItem> userItems = getCart(userId);
        if (userItems.isEmpty() && !cartStorage.containsKey(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for User ID: " + userId);
        }

        return userItems.stream()
                .mapToDouble(item -> {
                    Product product = productService.getProductById(item.getProductId());
                    if (product != null) {
                        return product.getPrice() * item.getQuantity();
                    }
                    return 0.0;
                })
                .sum();
    }
}
