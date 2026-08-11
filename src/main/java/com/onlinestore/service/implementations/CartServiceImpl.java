package com.onlinestore.service.implementations;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.CartDAO;
import com.onlinestore.DAO.interfaces.CartItemDAO;
import com.onlinestore.DAO.interfaces.ProductDAO;
import com.onlinestore.DAO.interfaces.UserDAO;
import com.onlinestore.api.entities.Cart;
import com.onlinestore.api.entities.CartItem;
import com.onlinestore.api.entities.Product;
import com.onlinestore.api.entities.User;
import com.onlinestore.service.ErrorHandling.GlobalNotFoundException;
import com.onlinestore.service.ErrorHandling.InsufficientStockException;
import com.onlinestore.service.interfaces.CartService;

@Service
public class CartServiceImpl implements CartService {

    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;

    public CartServiceImpl(CartDAO cartDAO, CartItemDAO cartItemDAO, ProductDAO productDAO, UserDAO userDAO) {
        this.cartDAO = cartDAO;
        this.cartItemDAO = cartItemDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public Cart getOrCreateCartForCustomer(int customerId) {
        Optional<Cart> existing = cartDAO.findByCustomerId(customerId);
        if (existing.isPresent()) {
            return existing.get();
        }
        User customer = userDAO.findById(customerId);
        if (customer == null) {
            throw new GlobalNotFoundException("User id not found - " + customerId);
        }
        return cartDAO.save(new Cart(customer));
    }

    @Override
    @Transactional
    public Cart addItem(int customerId, int productId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        Cart cart = getOrCreateCartForCustomer(customerId);
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new GlobalNotFoundException("Product id not found - " + productId);
        }

        Optional<CartItem> existingItem = cartItemDAO.findByCartIdAndProductId(cart.getId(), productId);
        int desiredQuantity = quantity + existingItem.map(CartItem::getQuantity).orElse(0);
        if (desiredQuantity > product.getStock()) {
            throw new InsufficientStockException("Not enough stock for product - " + product.getName());
        }

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(desiredQuantity);
            cartItemDAO.save(item);
        } else {
            cartItemDAO.save(new CartItem(cart, product, quantity));
        }
        return cartDAO.findById(cart.getId());
    }

    @Override
    @Transactional
    public Cart updateItemQuantity(int customerId, int productId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        Cart cart = getOrCreateCartForCustomer(customerId);
        CartItem item = cartItemDAO.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new GlobalNotFoundException("Product not in cart - " + productId));

        if (quantity > item.getProduct().getStock()) {
            throw new InsufficientStockException("Not enough stock for product - " + item.getProduct().getName());
        }
        item.setQuantity(quantity);
        cartItemDAO.save(item);
        return cartDAO.findById(cart.getId());
    }

    @Override
    @Transactional
    public Cart removeItem(int customerId, int productId) {
        Cart cart = getOrCreateCartForCustomer(customerId);
        CartItem item = cartItemDAO.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new GlobalNotFoundException("Product not in cart - " + productId));
        cartItemDAO.deleteById(item.getId());
        return cartDAO.findById(cart.getId());
    }
}
