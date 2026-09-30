package com.trainosys.shopapi.cart;

import com.trainosys.shopapi.product.Product;
import com.trainosys.shopapi.product.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController {

    private final CartServiceImpl cartService;
    private final ProductServiceImpl productService;

    public CartController(CartServiceImpl cartService, ProductServiceImpl productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/public/carts/{userId}")
    public ResponseEntity<List<CartItem>> viewCart(@PathVariable int userId) {
        return new ResponseEntity<>(cartService.getCart(userId), HttpStatus.OK);
    }

    @PostMapping("/public/carts/{userId}/items")
    public ResponseEntity<Object> addItemToCart(@PathVariable int userId, @RequestBody CartItem item) {
        try {
            cartService.addItem(userId, item);
            return new ResponseEntity<>(cartService.getCart(userId), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/public/carts/{userId}/items/{productId}")
    public ResponseEntity<Object> updateItemQuantity(@PathVariable int userId,
                                                     @PathVariable int productId,
                                                     @RequestBody CartItem item) {
        try {
            cartService.updateItemQuantity(userId, productId, item.getQuantity());
            return new ResponseEntity<>(cartService.getCart(userId), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @DeleteMapping("/public/carts/{userId}/items/{productId}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable int userId, @PathVariable int productId) {
        try {
            String status = cartService.removeItem(userId, productId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @DeleteMapping("/public/carts/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable int userId) {
        try {
            String status = cartService.clearCart(userId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @GetMapping("/admin/carts")
    public ResponseEntity<Map<Integer, List<CartItem>>> getAllCarts() {
        return new ResponseEntity<>(cartService.getAllCarts(), HttpStatus.OK);
    }

    @GetMapping("/public/carts/{userId}/total")
    public ResponseEntity<Object> getCartTotal(@PathVariable int userId) {
        try {
            double total = cartService.getCartTotal(userId);
            return new ResponseEntity<>(total, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
