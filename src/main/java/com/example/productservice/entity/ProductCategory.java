package com.example.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_CATEGORY",
        uniqueConstraints = {@UniqueConstraint(name = "uniqueCategory"
                , columnNames = {"topCategory", "category", "subcategory"})})
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID categoryId;
    private String topCategory;
    private String category;
    private String subcategory;
    private Date createDate;
    private Date updatedDate;
}
