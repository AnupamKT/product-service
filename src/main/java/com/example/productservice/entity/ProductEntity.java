package com.example.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCT_INFO"
        , uniqueConstraints = {@UniqueConstraint(name = "uniqueProductNameAndSeller"
        , columnNames = {"productName", "seller"})})
@JsonIgnoreProperties(ignoreUnknown = true)

public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;
    private UUID categoryId;
    private String productName;
    private double price;
    private String seller;
    private Date createdDate;
    private Date updatedDate;
}
