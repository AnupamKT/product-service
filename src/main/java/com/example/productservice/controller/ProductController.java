package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.model.ProductsAddRequest;
import com.example.productservice.model.Response;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /*
     * This API takes list of products to be added for putting up on sale
     * This API invokes inventory service to update inventory for the product name
     * This API check if product provided in request is already present in product table
     * if product is already present then it won't be persisted in product table,just inventory gets updated
     * if product is not already present then product details will be persisted and inventory will be updated.
     * This API uses productName and seller name to decide if product is present in DB or not
     * if product name and seller name is same, then it is considered as duplicate product entry.
     */
    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody ProductsAddRequest products) throws Exception {
        Response response = productService.addProduct(products);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public ResponseEntity Product(@RequestBody Product product) {
        return null;
    }
}