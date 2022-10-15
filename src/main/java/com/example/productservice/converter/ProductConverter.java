package com.example.productservice.converter;

import com.example.productservice.entity.ProductCategory;
import com.example.productservice.entity.ProductEntity;
import com.example.productservice.model.ProductGetResponse;
import com.example.productservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductConverter {

    @Autowired
    private CategoryRepository categoryRepository;

    public ProductGetResponse prepareGetResponse(ProductEntity productEntity) {
        ProductGetResponse response = new ProductGetResponse();

        Optional<ProductCategory> categoryOptional = categoryRepository
                .findById(productEntity.getCategoryId());
        ProductCategory category = categoryOptional.get();
        response.setProductName(productEntity.getProductName());
        response.setPrice(productEntity.getPrice());
        response.setSeller(productEntity.getSeller());
        response.setCategoryName(category.getCategoryName());
        return response;
    }
}
