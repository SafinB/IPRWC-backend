package com.example.IPRWCBackendHer.DAO;

import com.example.IPRWCBackendHer.models.Product;
import com.example.IPRWCBackendHer.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Component
public class ProductDao {
    private final ProductRepository productRepository;


    public ProductDao(ProductRepository productRepository) {
        this.productRepository = productRepository;

    }

    public void saveToDatabase(Product product) {
        this.productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.updateProduct(product.getName(), product.getDescription(), product.getPrice(), product.getId());
    }


    public void deleteProductById(UUID id) {
        this.productRepository.deleteById(id);
    }

    public ArrayList<Product> getAllProducts() {
        return (ArrayList<Product>) this.productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        return this.productRepository.findById(id).get();
    }
}
