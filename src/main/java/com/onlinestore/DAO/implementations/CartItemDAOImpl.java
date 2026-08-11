package com.onlinestore.DAO.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.CartItemDAO;
import com.onlinestore.api.entities.CartItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class CartItemDAOImpl implements CartItemDAO {

    private EntityManager entityManager;

    @Autowired
    public CartItemDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public CartItem save(CartItem theCartItem) {
        return entityManager.merge(theCartItem);
    }

    @Override
    public CartItem findById(int id) {
        return entityManager.find(CartItem.class, id);
    }

    @Override
    public Optional<CartItem> findByCartIdAndProductId(int cartId, int productId) {
        TypedQuery<CartItem> query = entityManager.createQuery(
                "FROM CartItem WHERE cart.id = :cartId AND product.id = :productId", CartItem.class);
        query.setParameter("cartId", cartId);
        query.setParameter("productId", productId);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        CartItem theCartItem = entityManager.find(CartItem.class, theId);
        entityManager.remove(theCartItem);
    }
}
