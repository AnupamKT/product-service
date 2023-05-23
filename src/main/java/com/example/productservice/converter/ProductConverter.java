package com.example.productservice.converter;

import com.example.productservice.entity.ProductCategory;
import com.example.productservice.entity.ProductEntity;
import com.example.productservice.model.Product;
import com.example.productservice.model.ProductGetResponse;
import com.example.productservice.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        //response.setCategoryName(category.getCategoryName());
        return response;
    }

    public List<ProductEntity> convertProductsTOProductEntityList(List<Product> products, String sellerName) {
        List<ProductEntity> productEntityList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Product product : products) {
            ProductEntity productEntity = mapper.convertValue(product, ProductEntity.class);
            productEntity.setSeller(sellerName);
            productEntity.setCreatedDate(new Date());
            productEntity.setUpdatedDate(new Date());
            productEntityList.add(productEntity);
        }
        return productEntityList;
    }
}
