package com.example.IPRWCBackendHer.repository;

import com.example.IPRWCBackendHer.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Modifying
    @Transactional
    @Query("update Product p set p.name = ?1, p.description = ?2, p.price = ?3 where p.id = ?4")
    void updateProduct(String name, String description, double price, UUID id);
}
