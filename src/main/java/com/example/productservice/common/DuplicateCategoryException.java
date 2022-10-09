package com.example.productservice.common;

public class DuplicateCategoryException extends Exception {
    public DuplicateCategoryException(String msg) {
        super(msg);
    }
}
