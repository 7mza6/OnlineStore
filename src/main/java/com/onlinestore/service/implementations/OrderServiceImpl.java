package com.onlinestore.service.implementations;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.CartDAO;
import com.onlinestore.DAO.interfaces.OrderDAO;
import com.onlinestore.DAO.interfaces.ProductDAO;
import com.onlinestore.DAO.interfaces.UserDAO;
import com.onlinestore.api.dto.PagedResult;
import com.onlinestore.api.entities.Cart;
import com.onlinestore.api.entities.CartItem;
import com.onlinestore.api.entities.Order;
import com.onlinestore.api.entities.OrderProduct;
import com.onlinestore.api.entities.Product;
import com.onlinestore.api.entities.User;
import com.onlinestore.service.ErrorHandling.AccessDeniedException;
import com.onlinestore.service.ErrorHandling.GlobalNotFoundException;
import com.onlinestore.service.ErrorHandling.InsufficientStockException;
import com.onlinestore.service.interfaces.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;

    public OrderServiceImpl(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO, UserDAO userDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public Order placeOrder(int customerId) {
        Optional<Cart> cartOpt = cartDAO.findByCustomerId(customerId);
        if (cartOpt.isEmpty() || cartOpt.get().getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        Cart cart = cartOpt.get();

        for (CartItem item : cart.getItems()) {
            Product product = productDAO.findById(item.getProduct().getId());
            if (item.getQuantity() > product.getStock()) {
                throw new InsufficientStockException("Not enough stock for product - " + product.getName());
            }
        }

        User customer = userDAO.findById(customerId);
        Order order = new Order(customer, BigDecimal.ZERO);
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            Product product = productDAO.findById(item.getProduct().getId());
            product.setStock(product.getStock() - item.getQuantity());
            productDAO.save(product);

            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(lineTotal);

            OrderProduct orderProduct = new OrderProduct(order, product, item.getQuantity(), product.getPrice());
            order.getItems().add(orderProduct);
        }
        order.setTotal(total);
        Order savedOrder = orderDAO.save(order);

        cart.getItems().clear();
        cartDAO.save(cart);

        return savedOrder;
    }

    @Override
    public Order findByIdForCustomer(int orderId, int customerId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new GlobalNotFoundException("Order id not found - " + orderId);
        }
        if (order.getCustomer().getId() != customerId) {
            throw new AccessDeniedException("You cannot view another customer's order");
        }
        return order;
    }

    @Override
    public Order findByIdForAdmin(int orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new GlobalNotFoundException("Order id not found - " + orderId);
        }
        return order;
    }

    @Override
    public PagedResult<Order> findByCustomerId(int customerId, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 20 : Math.min(size, 100);
        var content = orderDAO.findByCustomerId(customerId, safePage, safeSize);
        long total = orderDAO.countByCustomerId(customerId);
        return new PagedResult<>(content, safePage, safeSize, total);
    }

    @Override
    public PagedResult<Order> findAll(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 20 : Math.min(size, 100);
        var content = orderDAO.findAll(safePage, safeSize);
        long total = orderDAO.count();
        return new PagedResult<>(content, safePage, safeSize, total);
    }
}
