package com.onlinestore.service.implementations;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.CategoryDAO;
import com.onlinestore.api.entities.Category;
import com.onlinestore.service.ErrorHandling.GlobalNotFoundException;
import com.onlinestore.service.interfaces.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    @Transactional
    public Category save(Category theCategory) {
        return categoryDAO.save(theCategory);
    }

    @Override
    public Category findById(int id) {
        Category theCategory = categoryDAO.findById(id);
        if (theCategory == null) {
            throw new GlobalNotFoundException("Category id not found - " + id);
        }
        return theCategory;
    }

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        findById(theId);
        categoryDAO.deleteById(theId);
    }
}
