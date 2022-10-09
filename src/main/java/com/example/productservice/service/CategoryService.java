package com.example.productservice.service;

import com.example.productservice.common.DuplicateCategoryException;
import com.example.productservice.entity.ProductCategory;
import com.example.productservice.model.Response;
import com.example.productservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Response addCategory(String categoryName) throws DuplicateCategoryException {

        if (!checkIfCategoryNameAlreadyExists(categoryName)) {
            ProductCategory category = new ProductCategory();
            category.setCategoryName(categoryName);
            category.setCreateDate(new Date());
            categoryRepository.save(category);
        }
        String msg = "category added successfully";
        return new Response(201, msg);
    }

    private boolean checkIfCategoryNameAlreadyExists(String categoryName) throws DuplicateCategoryException {
        if (categoryRepository.existsProductCategoryByCategoryName(categoryName)) {
            String msg = "category already exists " + categoryName;
            throw new DuplicateCategoryException(msg);
        } else {
            return false;
        }
    }
}
