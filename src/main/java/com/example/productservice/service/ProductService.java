package com.example.productservice.service;

import com.example.productservice.common.InvalidProductException;
import com.example.productservice.common.ProductServiceConstants;
import com.example.productservice.common.ProductServiceException;
import com.example.productservice.converter.InventoryConverter;
import com.example.productservice.entity.ProductEntity;
import com.example.productservice.model.Product;
import com.example.productservice.model.ProductsAddRequest;
import com.example.productservice.model.Response;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.validator.ValidatorIF;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryServiceIF inventoryServiceIF;

    @Autowired
    private ValidatorIF validatorIF;

    @Autowired
    private InventoryConverter inventoryConverter;

    public Response addProduct(ProductsAddRequest products) throws Exception {
        //validate request
        validatorIF.validate(products);
        try {
            //some product might be already present in product DB
            //persist only if it is not available with same product name and seller name.
            List<ProductEntity> entityList = filterProductForPersistingInDB(products);
            productRepository.saveAll(entityList);
            //call inventory service to update inventory.
            updateInventory(products);
        } catch (Exception e) {
            handleException(e);
        }
        String msg = "Add product request completed successfully";
        return new Response(200, msg);
    }

    private void updateInventory(ProductsAddRequest product) throws ProductServiceException {
        String action = ProductServiceConstants.InventoryAction.ADD.toString();
        inventoryServiceIF
                .updateInventory(inventoryConverter.prepareInventoryRequest(product, action));
    }
    private List<ProductEntity> filterProductForPersistingInDB(ProductsAddRequest request) {
        List<ProductEntity> productEntityList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Product product : request.getProducts()) {
            Optional<ProductEntity> entityOptional = productRepository.
                    findByProductNameAndSeller(product.getProductName(), product.getSeller());
            if (!entityOptional.isPresent()) {
                ProductEntity productEntity = mapper.convertValue(product, ProductEntity.class);
                productEntity.setCreatedDate(new Date());
                productEntity.setUpdatedDate(new Date());
                productEntityList.add(productEntity);
            }
        }
        return productEntityList;
    }

    private void handleException(Exception e) throws Exception {
        if (e instanceof InvalidProductException) {
            throw new InvalidProductException("Invalid Product details!! " + e.getMessage());
        } else {
            String msg = "Server error occurred!! please try again " + e.getMessage();
            throw new ProductServiceException(msg);
        }
    }
}
