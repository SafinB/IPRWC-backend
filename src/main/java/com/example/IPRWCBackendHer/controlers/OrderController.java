package com.example.IPRWCBackendHer.controlers;

import com.example.IPRWCBackendHer.DAO.OrderDao;
import com.example.IPRWCBackendHer.models.ApiResponse;
import jakarta.persistence.criteria.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

public class OrderController {
    private final OrderDao orderDao;

    public OrderController(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse postOrder(@RequestBody ArrayList<Order> orders) {
        if (orders.size() < 1) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "You do not have items in your cart!");
        }
        for (Order order : orders) {
            this.orderDao.saveToDatabase(order);
        }
        return new ApiResponse(HttpStatus.ACCEPTED, "You posted some data!");
    }
}
