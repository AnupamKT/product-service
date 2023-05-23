package com.example.productservice.validator;

import com.example.productservice.common.InvalidProductException;
import com.example.productservice.model.ProductsAddRequest;
import org.springframework.stereotype.Component;

public interface ValidatorIF {

    void validate(Object request) throws Exception;
}
