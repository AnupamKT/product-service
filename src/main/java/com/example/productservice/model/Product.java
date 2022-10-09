package com.example.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String productName;
    private double price;
    private int quantity;
    private String seller;
    private String categoryName;
    private UUID categoryId;
}
