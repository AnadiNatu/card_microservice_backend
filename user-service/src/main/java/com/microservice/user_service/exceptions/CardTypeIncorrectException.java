package com.microservice.user_service.exceptions;

public class CardTypeIncorrectException extends IllegalStateException {

    private static final long serialVersionUID = 2;

    public CardTypeIncorrectException(String message){super(message);}
}
