package com.onlinestore.api.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinestore.api.dto.ReviewRequest;
import com.onlinestore.api.entities.Review;
import com.onlinestore.security.UserPrincipal;
import com.onlinestore.service.interfaces.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/product/{productId}")
    public List<Review> findByProduct(@PathVariable int productId) {
        return reviewService.findByProductId(productId);
    }

    @PostMapping("/product/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Review addReview(@AuthenticationPrincipal UserPrincipal principal, @PathVariable int productId,
            @RequestBody ReviewRequest request) {
        return reviewService.addReview(principal.getId(), productId, request.getRating(), request.getComment());
    }
}
