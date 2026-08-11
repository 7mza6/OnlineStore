package com.onlinestore.service.implementations;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.ProductDAO;
import com.onlinestore.DAO.interfaces.ReviewDAO;
import com.onlinestore.DAO.interfaces.UserDAO;
import com.onlinestore.api.entities.Product;
import com.onlinestore.api.entities.Review;
import com.onlinestore.api.entities.User;
import com.onlinestore.service.ErrorHandling.DuplicateResourceException;
import com.onlinestore.service.ErrorHandling.GlobalNotFoundException;
import com.onlinestore.service.interfaces.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewDAO reviewDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;

    public ReviewServiceImpl(ReviewDAO reviewDAO, ProductDAO productDAO, UserDAO userDAO) {
        this.reviewDAO = reviewDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public Review addReview(int customerId, int productId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new GlobalNotFoundException("Product id not found - " + productId);
        }
        User customer = userDAO.findById(customerId);
        if (customer == null) {
            throw new GlobalNotFoundException("User id not found - " + customerId);
        }
        if (reviewDAO.findByProductIdAndCustomerId(productId, customerId).isPresent()) {
            throw new DuplicateResourceException("You have already reviewed this product");
        }
        return reviewDAO.save(new Review(product, customer, rating, comment));
    }

    @Override
    public List<Review> findByProductId(int productId) {
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new GlobalNotFoundException("Product id not found - " + productId);
        }
        return reviewDAO.findByProductId(productId);
    }
}
