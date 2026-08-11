package com.onlinestore.DAO.interfaces;

import java.util.Optional;

import com.onlinestore.api.entities.CartItem;

public interface CartItemDAO {
    CartItem save(CartItem theCartItem);
    CartItem findById(int id);
    Optional<CartItem> findByCartIdAndProductId(int cartId, int productId);
    void deleteById(int theId);
}
