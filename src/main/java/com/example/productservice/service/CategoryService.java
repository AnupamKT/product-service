package com.example.productservice.service;

import com.example.productservice.converter.CategoryConverter;
import com.example.productservice.entity.ProductCategory;
import com.example.productservice.model.CategoryList;
import com.example.productservice.model.Response;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.validator.ValidatorIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    @Qualifier("category")
    private ValidatorIF validator;
    @Autowired
    private CategoryConverter converter;


    public Response addCategory(CategoryList categoryList) throws Exception {
        validator.validate(categoryList);
        List<ProductCategory> productCategoryList = converter.convertToProductCategoryEntity(categoryList);
        categoryRepository.saveAll(productCategoryList);
        String msg = "category added successfully";
        return new Response(201, msg);
    }
}
