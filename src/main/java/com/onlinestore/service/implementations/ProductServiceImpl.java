package com.onlinestore.service.implementations;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.CategoryDAO;
import com.onlinestore.DAO.interfaces.ProductDAO;
import com.onlinestore.api.dto.PagedResult;
import com.onlinestore.api.entities.Category;
import com.onlinestore.api.entities.Product;
import com.onlinestore.service.ErrorHandling.GlobalNotFoundException;
import com.onlinestore.service.interfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public ProductServiceImpl(ProductDAO productDAO, CategoryDAO categoryDAO) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
    }

    @Override
    @Transactional
    public Product save(Product theProduct) {
        int categoryId = theProduct.getCategory().getId();
        Category theCategory = categoryDAO.findById(categoryId);
        if (theCategory == null) {
            throw new GlobalNotFoundException("Category id not found - " + categoryId);
        }
        theProduct.setCategory(theCategory);
        return productDAO.save(theProduct);
    }

    @Override
    public Product findById(int id) {
        Product theProduct = productDAO.findById(id);
        if (theProduct == null) {
            throw new GlobalNotFoundException("Product id not found - " + id);
        }
        return theProduct;
    }

    @Override
    public PagedResult<Product> search(Integer categoryId, String keyword, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 20 : Math.min(size, 100);
        List<Product> content = productDAO.search(categoryId, keyword, safePage, safeSize);
        long total = productDAO.count(categoryId, keyword);
        return new PagedResult<>(content, safePage, safeSize, total);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        findById(theId);
        productDAO.deleteById(theId);
    }
}
