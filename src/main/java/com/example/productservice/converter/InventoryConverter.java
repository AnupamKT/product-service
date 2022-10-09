package com.example.productservice.converter;

import com.example.productservice.model.Inventory;
import com.example.productservice.model.ProductsAddRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryConverter {
    public List<Inventory> prepareInventoryRequest(ProductsAddRequest request, String action) {
        List<Inventory> inventoryList = new ArrayList<>();

        request.getProducts().forEach(product -> {
            Inventory inventory = new Inventory();
            inventory.setProductName(product.getProductName());
            inventory.setQuantity((long) product.getQuantity());
            inventory.setCategoryName(product.getCategoryName());
            inventory.setAction(action);
            inventoryList.add(inventory);
        });
        return inventoryList;
    }
}
