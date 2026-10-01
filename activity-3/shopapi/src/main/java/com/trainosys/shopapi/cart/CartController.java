package com.trainosys.shopapi.cart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    private final CartItemService cartService; // Depend on interface layout

    public CartController(CartItemService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/public/carts/{userId}")
    public ResponseEntity<List<CartItem>> viewCart(@PathVariable Long userId) {
        return new ResponseEntity<>(cartService.getCart(userId), HttpStatus.OK);
    }

    @PostMapping("/public/carts/{userId}/items")
    public ResponseEntity<Object> addItemToCart(@PathVariable Long userId, @RequestBody CartItem item) {
        try {
            cartService.addItem(userId, item);
            return new ResponseEntity<>(cartService.getCart(userId), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/public/carts/{userId}/items/{productId}")
    public ResponseEntity<Object> updateItemQuantity(@PathVariable Long userId,
                                                     @PathVariable Long productId,
                                                     @RequestBody CartItem item) {
        try {
            cartService.updateItemQuantity(userId, productId, item.getQuantity());
            return new ResponseEntity<>(cartService.getCart(userId), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @DeleteMapping("/public/carts/{userId}/items/{productId}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            String status = cartService.removeItem(userId, productId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @DeleteMapping("/public/carts/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        try {
            String status = cartService.clearCart(userId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @GetMapping("/admin/carts")
    public ResponseEntity<List<CartItem>> getAllCarts() {
        return new ResponseEntity<>(cartService.getAllCartItems(), HttpStatus.OK);
    }

    @GetMapping("/public/carts/{userId}/total")
    public ResponseEntity<Object> getCartTotal(@PathVariable Long userId) {
        try {
            double total = cartService.getCartTotal(userId);
            return new ResponseEntity<>(total, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
