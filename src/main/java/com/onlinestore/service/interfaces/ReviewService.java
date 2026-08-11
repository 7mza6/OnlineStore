package com.onlinestore.service.interfaces;

import java.util.List;

import com.onlinestore.api.entities.Review;

public interface ReviewService {
    Review addReview(int customerId, int productId, int rating, String comment);
    List<Review> findByProductId(int productId);
}
