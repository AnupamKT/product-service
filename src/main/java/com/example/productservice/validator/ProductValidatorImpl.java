package com.example.productservice.validator;

import com.example.productservice.common.InvalidProductException;
import com.example.productservice.handler.CategoryDataHandler;
import com.example.productservice.model.Category;
import com.example.productservice.model.Product;
import com.example.productservice.model.ProductsAddRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component("product")
@Slf4j
public class ProductValidatorImpl implements ValidatorIF {

    @Autowired
    private CategoryDataHandler dataHandler;

    @Override
    public void validate(Object object) throws InvalidProductException {
        List<String> errorMsg = new ArrayList<>();
        ProductsAddRequest request = null;
        if (object instanceof ProductsAddRequest) {
            request = (ProductsAddRequest) object;
        }
        //validate price and quantity--should be greater than zero
        Optional<Product> optionalProduct = validatePriceAndQuantity(request);
        if (optionalProduct.isPresent()) {
            String productName = optionalProduct.get().getProductName();
            String msg = "Invalid Product details!! for product " + productName + " quantity and price must be greater than zero";
            errorMsg.add(msg);
        }
        Map<UUID, Category> productCategory = dataHandler.getProductCategoryMap();
        //validate category for each product and populate categoryId for each product
        validateCategoryAndPopulateCategoryId(request, productCategory, errorMsg);
        if (!CollectionUtils.isEmpty(errorMsg)) {
            log.error(errorMsg.toString());
            throw new InvalidProductException(errorMsg.toString());
        }
    }

    /**
     * This method checks category object for each product and validate if it is present in DB
     * if category object is present, it populates the categoryId for that product.
     * if category is not present,exception is thrown.
     */
    private void validateCategoryAndPopulateCategoryId(ProductsAddRequest request, Map<UUID, Category> productCategory, List<String> errorMsg) throws InvalidProductException {
        Set<Map.Entry<UUID, Category>> entries = productCategory.entrySet();
        for (Product product : request.getProducts()) {
            Optional<Map.Entry<UUID, Category>> entryOptional = entries
                    .stream()
                    .filter(entry -> product.getCategory().equals(entry.getValue()))
                    .findAny();
            if (!entryOptional.isPresent()) {
                handleInvalidCategoryException(product, errorMsg);
            }
            //if there is no validation error, then set category Id
            if(CollectionUtils.isEmpty(errorMsg)){
                product.setCategoryId(entryOptional.get().getKey());
            }
        }
    }

    private void handleInvalidCategoryException(Product product, List<String> errorMsg) {
        Category category = product.getCategory();
        //validate top category
        boolean topCategoryCheck = dataHandler.getTopCategories()
                .stream()
                .anyMatch(tc -> tc.equalsIgnoreCase(category.getTopCategory()));
        if (!topCategoryCheck) {
            String msg = "Top category name " + category.getTopCategory() + " is not correct";
            errorMsg.add(msg);
        }
        boolean categoryCheck = dataHandler.getCategories()
                .stream()
                .anyMatch(tc -> tc.equalsIgnoreCase(category.getCategory()));
        if (!categoryCheck) {
            String msg = "Category name " + category.getCategory() + " is not correct";
            errorMsg.add(msg);
        }
        boolean subCategoryCheck = dataHandler.getSubCategories()
                .stream()
                .anyMatch(tc -> tc.equalsIgnoreCase(category.getSubcategory()));
        if (!subCategoryCheck) {
            String msg = "Sub category name " + category.getSubcategory() + " is not correct";
            errorMsg.add(msg);
        }
    }

    public Optional<Product> validatePriceAndQuantity(ProductsAddRequest request) {
        return request.
                getProducts()
                .stream()
                .filter(product -> (product.getQuantity() <= 0 || product.getPrice() <= 0))
                .findAny();
    }
}
