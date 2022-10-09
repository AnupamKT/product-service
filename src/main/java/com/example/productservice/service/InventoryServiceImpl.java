package com.example.productservice.service;

import com.example.productservice.common.InventoryServiceException;
import com.example.productservice.common.ProductServiceException;
import com.example.productservice.invoker.InventoryServiceInvoker;
import com.example.productservice.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class InventoryServiceImpl implements InventoryServiceIF {

    @Autowired
    private InventoryServiceInvoker invoker;

    @Override
    public void updateInventory(List<Inventory> inventorList) throws ProductServiceException {
        try {
            for (Inventory inventory : inventorList) {
                invoker.updateInventory(inventory);
            }
        } catch (Exception e) {
            //here rollback code will go for removing product entry from product info;
            String msg = "Inventory update failed";
            throw new ProductServiceException(msg);
        }
    }
}
