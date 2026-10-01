package com.trainosys.shopapi.product;


import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id); // Changed to Long
    String addProduct(Product product);
    Product updateProduct(Long id, Product productDetails); // Changed to Long
    String deleteProduct(Long id); // Changed to Long
    List<Product> getProductsByCategory(String category);
    Product updateStock(Long id, int quantity); // Changed to Long
}
