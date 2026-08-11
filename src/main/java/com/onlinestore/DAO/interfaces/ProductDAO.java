package com.onlinestore.DAO.interfaces;

import java.util.List;

import com.onlinestore.api.entities.Product;

public interface ProductDAO {
    Product save(Product theProduct);
    Product findById(int id);
    List<Product> findAll();
    List<Product> search(Integer categoryId, String keyword, int page, int size);
    long count(Integer categoryId, String keyword);
    void deleteById(int theId);
}
