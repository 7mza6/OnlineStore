package com.onlinestore.DAO.interfaces;

import java.util.Optional;

import com.onlinestore.api.entities.Cart;

public interface CartDAO {
    Cart save(Cart theCart);
    Cart findById(int id);
    Optional<Cart> findByCustomerId(int customerId);
    void deleteById(int theId);
}
