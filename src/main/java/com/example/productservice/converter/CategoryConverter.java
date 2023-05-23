package com.example.productservice.converter;

import com.example.productservice.entity.ProductCategory;
import com.example.productservice.model.Category;
import com.example.productservice.model.CategoryList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CategoryConverter {

    public List<ProductCategory> convertToProductCategoryEntity(CategoryList categoryList) {
        List<ProductCategory> productCategoryList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Category category : categoryList.getCategories()) {
            ProductCategory categoryEntity = mapper.convertValue(category, ProductCategory.class);
            categoryEntity.setCreateDate(new Date());
            categoryEntity.setUpdatedDate(new Date());
            productCategoryList.add(categoryEntity);
        }
        return productCategoryList;
    }
}
