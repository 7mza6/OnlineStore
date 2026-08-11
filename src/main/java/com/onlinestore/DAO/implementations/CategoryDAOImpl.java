package com.onlinestore.DAO.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.CategoryDAO;
import com.onlinestore.api.entities.Category;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class CategoryDAOImpl implements CategoryDAO {

    private EntityManager entityManager;

    @Autowired
    public CategoryDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Category save(Category theCategory) {
        return entityManager.merge(theCategory);
    }

    @Override
    public Category findById(int id) {
        return entityManager.find(Category.class, id);
    }

    @Override
    public List<Category> findAll() {
        TypedQuery<Category> query = entityManager.createQuery("FROM Category", Category.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        Category theCategory = entityManager.find(Category.class, theId);
        entityManager.remove(theCategory);
    }
}
