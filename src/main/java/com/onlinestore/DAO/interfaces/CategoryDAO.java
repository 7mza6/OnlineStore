package com.onlinestore.DAO.interfaces;

import java.util.List;

import com.onlinestore.api.entities.Category;

public interface CategoryDAO {
    Category save(Category theCategory);
    Category findById(int id);
    List<Category> findAll();
    void deleteById(int theId);
}
