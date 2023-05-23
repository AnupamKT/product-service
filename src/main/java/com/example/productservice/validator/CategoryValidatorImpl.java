package com.example.productservice.validator;

import com.example.productservice.common.ConflictingCategoryException;
import com.example.productservice.common.DuplicateCategoryException;
import com.example.productservice.handler.CategoryDataHandler;
import com.example.productservice.model.Category;
import com.example.productservice.model.CategoryList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component("category")
@Slf4j
public class CategoryValidatorImpl implements ValidatorIF {
    @Autowired
    private CategoryDataHandler dataHandler;

    @Override
    public void validate(Object request) throws Exception {
        if (request instanceof CategoryList) {
            CategoryList categoryList = (CategoryList) request;
            List<Category> list = categoryList.getCategories();
            for (Category category : list) {
                //validate if top category exists in DB.if not exists then throw exception
                validateIfTopCategoryExists(category);
                //Validate if category object is not already present.
                validateIfCategoryAlreadyPresent(category);
            }
        }
    }

    private void validateIfCategoryAlreadyPresent(Category category) throws DuplicateCategoryException {
        boolean categoryCheck = false;
        boolean subCategoryCheck = false;
        List<String> categories = dataHandler.getCategories();
        if (!CollectionUtils.isEmpty(categories)) {
            categoryCheck = categories
                    .stream()
                    .anyMatch(ct -> category.getCategory().equalsIgnoreCase(ct));
        }
        List<String> subCategories = dataHandler.getSubCategories();
        if (!CollectionUtils.isEmpty(subCategories)) {
            subCategoryCheck = subCategories
                    .stream()
                    .anyMatch(subCat -> category.getSubcategory().equalsIgnoreCase(subCat));
        }
        if (categoryCheck && subCategoryCheck) {
            //throw exception when category and subcategory is already present in DB
            String msg = "Category " + category + "is already present";
            log.error(msg);
            throw new DuplicateCategoryException("Invalid request!!" + msg);
        }
    }

    private void validateIfTopCategoryExists(Category category) throws ConflictingCategoryException {
        List<String> topCategories = dataHandler.getTopCategories();
        if (!CollectionUtils.isEmpty(topCategories)) {
            boolean check = topCategories
                    .stream()
                    .anyMatch(tc -> category.getTopCategory().equalsIgnoreCase(tc));
            if (!check) {
                //throw exception when there is no matching top category
                String msg = "Matching top category " + category.getTopCategory() + " not found";
                log.error(msg);
                throw new ConflictingCategoryException("invalid request!! " + msg);
            }
        }
    }
}
