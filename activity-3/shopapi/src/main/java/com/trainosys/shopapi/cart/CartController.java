package com.trainosys.shopapi.cart;
import com.trainosys.shopapi.product.Product;
import com.trainosys.shopapi.product.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartItemService cartService;
    private final ProductService productService;

    public CartController(CartItemService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/{userId}")
    public List<CartItem> viewCart(@PathVariable int userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/{userId}/items")
    public List<CartItem> addItemToCart(@PathVariable int userId, @RequestBody CartItem item) {
        cartService.addItem(userId, item);
        return cartService.getCart(userId);
    }

    @PutMapping("/{userId}/items/{productId}")
    public List<CartItem> updateItemQuantity(@PathVariable int userId,
                                             @PathVariable int productId,
                                             @RequestBody CartItem item) {
        cartService.updateItemQuantity(userId, productId, item.getQuantity());
        return cartService.getCart(userId);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public String removeItemFromCart(@PathVariable int userId, @PathVariable int productId) {
        cartService.removeItem(userId, productId);
        return "Product ID " + productId + " removed from User " + userId + "'s cart.";
    }

    @DeleteMapping("/{userId}")
    public String clearCart(@PathVariable int userId) {
        cartService.clearCart(userId);
        return "Cart for User " + userId + " has been successfully cleared.";
    }

    @GetMapping("/{userId}/total")
    public double getCartTotal(@PathVariable int userId) {
        List<CartItem> userItems = cartService.getCart(userId);

        return userItems.stream()
                .mapToDouble(item -> {
                    // Look up the product details from the product module memory bank
                    Product product = productService.getProductById(item.getProductId());
                    if (product != null) {
                        return product.getPrice() * item.getQuantity();
                    }
                    return 0.0;
                })
                .sum();
    }
}