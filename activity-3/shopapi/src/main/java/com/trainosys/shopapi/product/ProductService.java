package com.trainosys.shopapi.product;


import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(int id);
    String addProduct(Product product);
    Product updateProduct(int id, Product productDetails);
    String deleteProduct(int id);
    List<Product> getProductsByCategory(String category);
    Product updateStock(int id, int quantity);
}
