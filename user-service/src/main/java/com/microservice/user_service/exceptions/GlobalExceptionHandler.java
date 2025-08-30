package com.microservice.user_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CardRelatedFieldIncorrectException.class)
    public ResponseEntity<ErrorObject> handleCardRelatedFieldIncorrectException(CardRelatedFieldIncorrectException ex){

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NO_CONTENT.value());
        errorObject.setTimestamp(new Date());
        errorObject.setMessage(ex.getMessage());

        return new ResponseEntity<ErrorObject>(errorObject  , HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(CardTypeIncorrectException.class)
    public ResponseEntity<ErrorObject> handleCardTypeIncorrectException(CardTypeIncorrectException ex){

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
        errorObject.setTimestamp(new Date());
        errorObject.setMessage(ex.getMessage());

        return new ResponseEntity<ErrorObject>(errorObject  , HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ErrorObject> handleCardNotFoundException(CardNotFoundException ex){

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
        errorObject.setTimestamp(new Date());
        errorObject.setMessage(ex.getMessage());

        return new ResponseEntity<ErrorObject>(errorObject  , HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException(UserNotFoundException ex){

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
        errorObject.setTimestamp(new Date());
        errorObject.setMessage(ex.getMessage());

        return new ResponseEntity<ErrorObject>(errorObject  , HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorObject> handleResourceNotFoundException(ResourceNotFoundException ex){

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
        errorObject.setTimestamp(new Date());
        errorObject.setMessage(ex.getMessage());

        return new ResponseEntity<ErrorObject>(errorObject  , HttpStatus.NO_CONTENT);
    }

}


