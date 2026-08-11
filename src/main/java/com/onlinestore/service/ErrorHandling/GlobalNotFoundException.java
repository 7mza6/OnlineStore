package com.onlinestore.service.ErrorHandling;

public class GlobalNotFoundException extends RuntimeException {

    public GlobalNotFoundException(String message) {
        super(message);
    }
}
