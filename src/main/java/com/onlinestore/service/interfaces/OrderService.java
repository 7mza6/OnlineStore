package com.onlinestore.service.interfaces;

import com.onlinestore.api.dto.PagedResult;
import com.onlinestore.api.entities.Order;

public interface OrderService {
    Order placeOrder(int customerId);
    Order findByIdForCustomer(int orderId, int customerId);
    Order findByIdForAdmin(int orderId);
    PagedResult<Order> findByCustomerId(int customerId, int page, int size);
    PagedResult<Order> findAll(int page, int size);
}
