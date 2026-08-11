package com.onlinestore.service.interfaces;

import com.onlinestore.api.entities.Cart;

public interface CartService {
    Cart getOrCreateCartForCustomer(int customerId);
    Cart addItem(int customerId, int productId, int quantity);
    Cart updateItemQuantity(int customerId, int productId, int quantity);
    Cart removeItem(int customerId, int productId);
}
