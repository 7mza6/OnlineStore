package com.onlinestore.DAO.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.CartDAO;
import com.onlinestore.api.entities.Cart;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class CartDAOImpl implements CartDAO {

    private EntityManager entityManager;

    @Autowired
    public CartDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Cart save(Cart theCart) {
        return entityManager.merge(theCart);
    }

    @Override
    public Cart findById(int id) {
        return entityManager.find(Cart.class, id);
    }

    @Override
    public Optional<Cart> findByCustomerId(int customerId) {
        TypedQuery<Cart> query = entityManager.createQuery(
                "FROM Cart WHERE customer.id = :customerId", Cart.class);
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
        Cart theCart = entityManager.find(Cart.class, theId);
        entityManager.remove(theCart);
    }
}
