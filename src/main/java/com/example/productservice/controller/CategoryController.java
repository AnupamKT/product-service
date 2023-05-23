package com.example.productservice.controller;

import com.example.productservice.model.CategoryList;
import com.example.productservice.model.Response;
import com.example.productservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add/categories")
    public ResponseEntity<Response> addCategory(@RequestBody CategoryList categoryList) throws Exception {
       Response response= categoryService.addCategory(categoryList);
        return ResponseEntity.status(201).body(response);
    }
}
