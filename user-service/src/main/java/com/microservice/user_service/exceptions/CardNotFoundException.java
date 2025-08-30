package com.microservice.user_service.exceptions;

import java.util.NoSuchElementException;

public class CardNotFoundException extends NoSuchElementException {

    private static final long serialVersionUID = 3;

    public CardNotFoundException(String message){
        super(message);
    }
}
