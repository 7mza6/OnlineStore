package com.onlinestore.DAO.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.ProductDAO;
import com.onlinestore.api.entities.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class ProductDAOImpl implements ProductDAO {

    private EntityManager entityManager;

    @Autowired
    public ProductDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Product save(Product theProduct) {
        return entityManager.merge(theProduct);
    }

    @Override
    public Product findById(int id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    public List<Product> findAll() {
        TypedQuery<Product> query = entityManager.createQuery("FROM Product", Product.class);
        return query.getResultList();
    }

    @Override
    public List<Product> search(Integer categoryId, String keyword, int page, int size) {
        StringBuilder jpql = new StringBuilder("FROM Product p WHERE 1=1");
        appendFilters(jpql, categoryId, keyword);
        jpql.append(" ORDER BY p.id");

        TypedQuery<Product> query = entityManager.createQuery(jpql.toString(), Product.class);
        setFilterParams(query, categoryId, keyword);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public long count(Integer categoryId, String keyword) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE 1=1");
        appendFilters(jpql, categoryId, keyword);

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        setFilterParams(query, categoryId, keyword);
        return query.getSingleResult();
    }

    private void appendFilters(StringBuilder jpql, Integer categoryId, String keyword) {
        if (categoryId != null) {
            jpql.append(" AND p.category.id = :categoryId");
        }
        if (keyword != null && !keyword.isBlank()) {
            jpql.append(" AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))");
        }
    }

    private void setFilterParams(TypedQuery<?> query, Integer categoryId, String keyword) {
        if (categoryId != null) {
            query.setParameter("categoryId", categoryId);
        }
        if (keyword != null && !keyword.isBlank()) {
            query.setParameter("keyword", keyword);
        }
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        Product theProduct = entityManager.find(Product.class, theId);
        entityManager.remove(theProduct);
    }
}
