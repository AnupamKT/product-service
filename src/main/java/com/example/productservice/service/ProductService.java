package com.example.productservice.service;

import com.example.productservice.common.InvalidProductException;
import com.example.productservice.common.ProductNotFoundException;
import com.example.productservice.common.ProductServiceConstants;
import com.example.productservice.common.ProductServiceException;
import com.example.productservice.converter.InventoryConverter;
import com.example.productservice.converter.ProductConverter;
import com.example.productservice.entity.ProductEntity;
import com.example.productservice.model.Product;
import com.example.productservice.model.ProductGetResponse;
import com.example.productservice.model.ProductsAddRequest;
import com.example.productservice.model.Response;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.util.CSVUtil;
import com.example.productservice.validator.ValidatorIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryServiceIF inventoryServiceIF;
    @Autowired
    @Qualifier("product")
    private ValidatorIF validatorIF;
    @Autowired
    private InventoryConverter inventoryConverter;
    @Autowired
    private ProductConverter productConverter;

    public Response addProduct(ProductsAddRequest products) throws Exception {
        //validate request
        validatorIF.validate(products);
        try {
            persistProductInDB(products);
            //call inventory service to update inventory.
            //updateInventory(products);

        } catch (Exception e) {
            handleException(e);
        }
        String msg = "Add product request completed successfully";
        return new Response(200, msg);
    }

    private void updateInventory(ProductsAddRequest product) throws ProductServiceException {
        String action = ProductServiceConstants.InventoryAction.ADD.toString();
        inventoryServiceIF.updateInventory(inventoryConverter.prepareInventoryRequest(product, action));
    }

    private void persistProductInDB(ProductsAddRequest request) {
        List<ProductEntity> productEntityList = null;
        String sellerName = request.getSellerName();
        List<ProductEntity> allProductsBySellerName =
                getAllProductsBySellerName(sellerName);
        if (CollectionUtils.isEmpty(allProductsBySellerName)) {
            //add everything, it means seller is adding product for the first time.
            productEntityList = productConverter.convertProductsTOProductEntityList(request.getProducts(), sellerName);
        } else {
            List<Product> filteredProducts = filterOutExistingProducts(allProductsBySellerName,
                    request.getProducts());
            productEntityList = productConverter.convertProductsTOProductEntityList(filteredProducts, sellerName);
        }
        productRepository.saveAll(productEntityList);
    }

    /**
     * This method compares two products list one which is submitted by seller and other list which is present ind DB
     * This method returns all products which is not present in list which is coming from DB.
     *
     * @return
     */
    private List<Product> filterOutExistingProducts(List<ProductEntity> productEntity, List<Product> products) {
        List<String> productNameList = productEntity.stream()
                .map(product -> product.getProductName().toLowerCase())
                .collect(Collectors.toList());
        return products.stream().
                filter(product -> !productNameList.contains(product.getProductName().toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<ProductEntity> getAllProductsBySellerName(String sellerName) {
        return productRepository.findBySeller(sellerName);
    }

    private void handleException(Exception e) throws Exception {
        if (e instanceof InvalidProductException) {
            throw new InvalidProductException("Invalid Product details!! " + e.getMessage());
        } else {
            String msg = "Server error occurred!! please try again " + e.getMessage();
            throw new ProductServiceException(msg);
        }
    }

    public Response findProduct(UUID productId) throws ProductNotFoundException {
        ProductGetResponse getResponse = null;
        Optional<ProductEntity> productEntityOptional = productRepository.findById(productId);
        if (productEntityOptional.isPresent()) {
            ProductEntity productEntity = productEntityOptional.get();

            getResponse = productConverter.prepareGetResponse(productEntity);

        } else {
            String msg = "product not found with productId " + productId;
            throw new ProductNotFoundException(msg);
        }
        return new Response(200, getResponse);
    }

    public Response fetchAll(int pageSize, int pageNumber) {
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("productName").ascending());
        Page<ProductEntity> productEntityList = productRepository.findAll(page);
        return new Response(200, productEntityList.getContent());
    }
}
