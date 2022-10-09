package com.example.productservice.common;

public class InvalidProductException extends Exception {
    public InvalidProductException(String msg) {
        super(msg);
    }
}
