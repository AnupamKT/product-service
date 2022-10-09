package com.example.productservice.validator;

import com.example.productservice.common.InvalidProductException;
import com.example.productservice.model.ProductsAddRequest;

public interface ValidatorIF {

    void validate(ProductsAddRequest request) throws InvalidProductException;
}
