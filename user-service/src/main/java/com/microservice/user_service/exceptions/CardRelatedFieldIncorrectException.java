package com.microservice.user_service.exceptions;

public class CardRelatedFieldIncorrectException extends IllegalArgumentException {

    private static final long serialVersionUID = 1;

    public CardRelatedFieldIncorrectException(String message){
        super(message);
    }

}
