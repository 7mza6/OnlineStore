package com.onlinestore.service.interfaces;

import com.onlinestore.api.dto.PagedResult;
import com.onlinestore.api.entities.Product;

public interface ProductService {
    Product save(Product theProduct);
    Product findById(int id);
    PagedResult<Product> search(Integer categoryId, String keyword, int page, int size);
    void deleteById(int theId);
}
