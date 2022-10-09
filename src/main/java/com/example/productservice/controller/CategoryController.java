package com.example.productservice.controller;

import com.example.productservice.common.DuplicateCategoryException;
import com.example.productservice.model.Response;
import com.example.productservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/{categoryName}")
    public ResponseEntity<Response> addCategory(@PathVariable String categoryName) throws DuplicateCategoryException {
       Response response= categoryService.addCategory(categoryName);
        return ResponseEntity.status(201).body(response);
    }
}
