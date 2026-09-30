package com.trainosys.shopapi.product;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductServiceImpl implements ProductService {
    private final List<Product> productList = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public ProductServiceImpl() {
        Product p1 = new Product();
        p1.setId(idGenerator.getAndIncrement());
        p1.setName("Wireless Mouse");
        p1.setPrice(499.0);
        p1.setCategory("Electronics");
        p1.setStock(50);

        Product p2 = new Product();
        p2.setId(idGenerator.getAndIncrement());
        p2.setName("Mechanical Keyboard");
        p2.setPrice(1200.0);
        p2.setCategory("Electronics");
        p2.setStock(20);

        productList.add(p1);
        productList.add(p2);
    }

    public List<Product> getAllProducts() {
        return productList;
    }

    public Product getProductById(int id) {
        return productList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id));
    }

    public String addProduct(Product product) {
        if (product == null || product.getName() == null || product.getName().trim().isEmpty() || product.getPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input details provided for product.");
        }
        product.setId(idGenerator.getAndIncrement());
        productList.add(product);
        return "Product added successfully";
    }

    public Product updateProduct(int id, Product productDetails) {
        if (productDetails == null || productDetails.getName() == null || productDetails.getName().trim().isEmpty() || productDetails.getPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input details provided for product update.");
        }
        Product existingProduct = productList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id));

        existingProduct.setName(productDetails.getName());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setStock(productDetails.getStock());
        return existingProduct;
    }

    public String deleteProduct(int id) {
        boolean removed = productList.removeIf(p -> p.getId() == id);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id);
        }
        return "Product with ID " + id + " has been successfully deleted.";
    }

    public List<Product> getProductsByCategory(String category) {
        return productList.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public Product updateStock(int id, int quantity) {
        if (quantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock quantity cannot be negative.");
        }
        Product existingProduct = productList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id));

        existingProduct.setStock(quantity);
        return existingProduct;
    }
}
