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
@Table(name="PRODUCT_CATEGORY")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID categoryId;
    @Column(unique = true,nullable = false)
    private String categoryName;
    private Date createDate;
}
