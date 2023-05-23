package com.example.productservice.util;

import com.example.productservice.common.CategoryServiceException;
import com.example.productservice.model.Category;
import com.example.productservice.model.CategoryList;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVUtil {
    /**
     * This method takes folder as a input and read all csv files present inside that folder.
     * All rows from CSV files are converted in to Category Beans.
     * returns a list containing all categories present inside files under that folder.
     * enhancement: to make this dynamic so that it can be converted to any bean type.
     */
    public static CategoryList parseCSVToBean(File dir) throws CategoryServiceException {
        CategoryList categoryList = new CategoryList();
        List<Category> list = new ArrayList<>();
        File[] files = dir.listFiles();
        for (File file : files) {
            List<Category> categories = null;
            try {
                categories = new CsvToBeanBuilder(new FileReader(file))
                        .withType(Category.class)
                        .build()
                        .parse();
            } catch (FileNotFoundException ex) {
                log.error("error occurred !! files not found");
                throw new CategoryServiceException("category files not found");
            }
            list.addAll(categories);
        }
        categoryList.setCategories(list);
        return categoryList;
    }
}
