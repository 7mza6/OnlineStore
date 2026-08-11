package com.onlinestore.DAO.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.OrderDAO;
import com.onlinestore.api.entities.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private EntityManager entityManager;

    @Autowired
    public OrderDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Order save(Order theOrder) {
        return entityManager.merge(theOrder);
    }

    @Override
    public Order findById(int id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public List<Order> findByCustomerId(int customerId, int page, int size) {
        TypedQuery<Order> query = entityManager.createQuery(
                "FROM Order WHERE customer.id = :customerId ORDER BY id DESC", Order.class);
        query.setParameter("customerId", customerId);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public long countByCustomerId(int customerId) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(o) FROM Order o WHERE o.customer.id = :customerId", Long.class);
        query.setParameter("customerId", customerId);
        return query.getSingleResult();
    }

    @Override
    public List<Order> findAll(int page, int size) {
        TypedQuery<Order> query = entityManager.createQuery("FROM Order ORDER BY id DESC", Order.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(o) FROM Order o", Long.class);
        return query.getSingleResult();
    }
}
