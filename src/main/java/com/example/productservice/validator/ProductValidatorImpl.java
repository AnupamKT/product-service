package com.example.productservice.validator;

import com.example.productservice.common.InvalidProductException;
import com.example.productservice.entity.ProductCategory;
import com.example.productservice.model.Product;
import com.example.productservice.model.ProductsAddRequest;
import com.example.productservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductValidatorImpl implements ValidatorIF {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void validate(ProductsAddRequest request) throws InvalidProductException {
        //validate price and quantity--should be greater than zero
        Optional<Product> optionalProduct = validatePriceAndQuantity(request);
        if (optionalProduct.isPresent()) {
            String msg = "Invalid Product details!! quantity and price must be greater than zero ";
            throw new InvalidProductException(msg);
        }
        //validate categoryName from category table
        List<ProductCategory> productCategory = categoryRepository.findAll();
        Optional<Product> productOptional = validateCategoryName(request,productCategory);
        if (productOptional.isPresent()) {
            String msg = "Category name is not correct for one or more products ";
            throw new InvalidProductException(msg);
        }
        //set categoryId for each product
        populateCategoryId(request,productCategory);
    }

    private void populateCategoryId(ProductsAddRequest request, List<ProductCategory> productCategory) {
        //converting to map for faster searching
       Map<String, UUID> map= productCategory
                .stream()
                .collect(Collectors.toMap(ProductCategory::getCategoryName,ProductCategory::getCategoryId));
       //setting categoryId for each product in request
        request.getProducts().
                forEach(product-> product.setCategoryId(map.get(product.getCategoryName())));
    }



    private Optional<Product> validateCategoryName(ProductsAddRequest request, List<ProductCategory> productCategory) {
        List<String> categoryList = productCategory
                .stream()
                .map(productCat -> productCat.getCategoryName())
                .collect(Collectors.toList());

        Optional<Product> optionalProduct = request
                .getProducts()
                .stream()
                .filter(product -> !categoryList.contains(product.getCategoryName()))
                .findAny();
        return optionalProduct;
    }

    public Optional<Product> validatePriceAndQuantity(ProductsAddRequest request) {
        return request.
                getProducts()
                .stream()
                .filter(product -> (product.getQuantity() <= 0 || product.getPrice() <= 0))
                .findAny();
    }

}
