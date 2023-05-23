package com.example.productservice.handler;

import com.example.productservice.entity.ProductCategory;
import com.example.productservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.productservice.common.ProductServiceConstants.*;

@Component
public class CategoryDataHandler {

    private CategoryRepository categoryRepository;
    private Map<String, List<String>> result = null;

    @Autowired
    CategoryDataHandler(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        result = loadCategories();
    }

    private Map<String, List<String>> loadCategories() {
        //{"topCategory:[fashion,electronics]","category:[Mens wear,Mobile,Laptop]"
        // ,"subcategory":[shirt,pant]}
        Map<String, List<String>> result = new HashMap<>();
        List<ProductCategory> categories = categoryRepository.findAll();
        if (!CollectionUtils.isEmpty(categories)) {
            //get top categories values
            List<String> topCategories = categories
                    .stream()
                    .map(productCategory -> productCategory.getTopCategory())
                    .map(str -> str.toLowerCase())
                    .distinct()
                    .collect(Collectors.toList());
            result.put(TOP_CATEGORY, topCategories);
            //get category value
            List<String> category = categories
                    .stream()
                    .map(productCategory -> productCategory.getCategory())
                    .map(str -> str.toLowerCase())
                    .distinct()
                    .collect(Collectors.toList());
            result.put(CATEGORY, category);
            //get sub categories values
            List<String> subCategories = categories
                    .stream()
                    .map(productCategory -> productCategory.getSubcategory())
                    .map(str -> str.toLowerCase())
                    .distinct()
                    .collect(Collectors.toList());
            result.put(SUB_CATEGORY, subCategories);
        }
        return result;
    }

    public List<String> getTopCategories() {
        return result.get(TOP_CATEGORY);
    }

    public List<String> getCategories() {
        return result.get(CATEGORY);
    }

    public List<String> getSubCategories() {
        return result.get(SUB_CATEGORY);
    }

}
