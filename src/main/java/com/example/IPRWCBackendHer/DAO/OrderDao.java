package com.example.IPRWCBackendHer.DAO;

import com.example.IPRWCBackendHer.repository.OrderRepository;
import jakarta.persistence.criteria.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Component
public class OrderDao {
    private final OrderRepository orderRepository;

    public OrderDao(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveToDatabase(Order order) {
        this.orderRepository.save(order);
    }
    public ArrayList<Order> getAllOrders() {
        return (ArrayList<Order>) this.orderRepository.findAll();
    }
    public void deleteOrderFromDatabase(UUID id) {
        this.orderRepository.deleteById(id);
    }
}
