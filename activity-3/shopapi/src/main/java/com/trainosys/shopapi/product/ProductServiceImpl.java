package com.trainosys.shopapi.product;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    // Inject the Repository instead of using an in-memory list
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id));
    }

    @Override
    public String addProduct(Product product) {
        if (product == null || product.getProductName() == null || product.getProductName().trim().isEmpty() || product.getPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input details provided for product.");
        }
        productRepository.save(product);
        return "Product added successfully";
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        if (productDetails == null || productDetails.getProductName() == null || productDetails.getProductName().trim().isEmpty() || productDetails.getPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input details provided for product update.");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id));

        existingProduct.setProductName(productDetails.getProductName());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setStock(productDetails.getStock());

        return productRepository.save(existingProduct);
    }

    @Override
    public String deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
        return "Product with ID " + id + " has been successfully deleted.";
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    @Override
    public Product updateStock(Long id, int quantity) {
        if (quantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock quantity cannot be negative.");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id));

        existingProduct.setStock(quantity);
        return productRepository.save(existingProduct);
    }
}
