package com.onlinestore.DAO.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.ReviewDAO;
import com.onlinestore.api.entities.Review;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class ReviewDAOImpl implements ReviewDAO {

    private EntityManager entityManager;

    @Autowired
    public ReviewDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Review save(Review theReview) {
        return entityManager.merge(theReview);
    }

    @Override
    public Review findById(int id) {
        return entityManager.find(Review.class, id);
    }

    @Override
    public List<Review> findByProductId(int productId) {
        TypedQuery<Review> query = entityManager.createQuery(
                "FROM Review WHERE product.id = :productId ORDER BY id DESC", Review.class);
        query.setParameter("productId", productId);
        return query.getResultList();
    }

    @Override
    public Optional<Review> findByProductIdAndCustomerId(int productId, int customerId) {
        TypedQuery<Review> query = entityManager.createQuery(
                "FROM Review WHERE product.id = :productId AND customer.id = :customerId", Review.class);
        query.setParameter("productId", productId);
        query.setParameter("customerId", customerId);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        Review theReview = entityManager.find(Review.class, theId);
        entityManager.remove(theReview);
    }
}
