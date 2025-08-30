package com.microservice.user_service.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private static final long seriesVersionUID = 6;

    public ResourceNotFoundException(String message){super(message);}

}
