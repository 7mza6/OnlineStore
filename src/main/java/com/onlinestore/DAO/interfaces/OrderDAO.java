package com.onlinestore.DAO.interfaces;

import java.util.List;

import com.onlinestore.api.entities.Order;

public interface OrderDAO {
    Order save(Order theOrder);
    Order findById(int id);
    List<Order> findByCustomerId(int customerId, int page, int size);
    long countByCustomerId(int customerId);
    List<Order> findAll(int page, int size);
    long count();
}
