package com.onlinestore.DAO.interfaces;

import java.util.List;
import java.util.Optional;

import com.onlinestore.api.entities.Review;

public interface ReviewDAO {
    Review save(Review theReview);
    Review findById(int id);
    List<Review> findByProductId(int productId);
    Optional<Review> findByProductIdAndCustomerId(int productId, int customerId);
    void deleteById(int theId);
}
