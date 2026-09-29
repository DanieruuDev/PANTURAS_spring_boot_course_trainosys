package com.trainosys.shopapi.product;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final List<Product> productList = new CopyOnWriteArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public ProductService() {
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
                .orElse(null);
    }

    public void addProduct(Product product) {
        product.setId(idGenerator.getAndIncrement());
        productList.add(product);
    }

    public void updateProduct(int id, Product productDetails) {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            existingProduct.setName(productDetails.getName());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setCategory(productDetails.getCategory());
            existingProduct.setStock(productDetails.getStock());
        }
    }

    public void deleteProduct(int id) {
        productList.removeIf(p -> p.getId() == id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productList.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public void updateStock(int id, int quantity) {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            existingProduct.setStock(quantity);
        }
    }

}
