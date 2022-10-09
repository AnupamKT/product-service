package com.example.productservice.service;

import com.example.productservice.common.ProductServiceException;
import com.example.productservice.model.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryServiceIF {

    void updateInventory(List<Inventory> inventorList) throws ProductServiceException;
}
