package com.trainosys.shopapi.cart;

import com.trainosys.shopapi.product.Product;
import com.trainosys.shopapi.product.ProductService;
import com.trainosys.shopapi.users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    public CartServiceImpl(CartItemRepository cartItemRepository, ProductService productService, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));
    }

    @Override
    public List<CartItem> getCart(Long userId) {
        validateUser(userId);
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void addItem(Long userId, CartItem newItem) {
        if (newItem == null || newItem.getQuantity() <= 0 || newItem.getProductId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input details provided for product or quantity.");
        }

        validateUser(userId);
        productService.getProductById(newItem.getProductId());

        Optional<CartItem> existingItemOpt = cartItemRepository.findByUserIdAndProductId(userId, newItem.getProductId());

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            newItem.setUserId(userId);
            cartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional
    public String updateItemQuantity(Long userId, Long productId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero.");
        }

        validateUser(userId);

        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found."));

        item.setQuantity(newQuantity);
        cartItemRepository.save(item);
        return "Quantity updated successfully";
    }

    @Override
    @Transactional
    public String removeItem(Long userId, Long productId) {
        validateUser(userId);

        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found."));

        cartItemRepository.delete(item);
        return "Product removed from cart.";
    }

    @Override
    @Transactional
    public String clearCart(Long userId) {
        validateUser(userId);

        List<CartItem> items = cartItemRepository.findByUserId(userId);
        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart is already empty.");
        }
        cartItemRepository.deleteByUserId(userId);
        return "Cart cleared successfully.";
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public double getCartTotal(Long userId) {
        validateUser(userId);

        List<CartItem> userItems = cartItemRepository.findByUserId(userId);
        if (userItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart is empty.");
        }

        return userItems.stream()
                .mapToDouble(item -> {
                    Product product = productService.getProductById(item.getProductId());
                    return product.getPrice() * item.getQuantity();
                })
                .sum();
    }
}
