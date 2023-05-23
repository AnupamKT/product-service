package com.example.productservice.model;

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
    private String categoryName;
    //gets populated from validator method.
    private UUID categoryId;
}
