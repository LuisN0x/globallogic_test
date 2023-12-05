package com.desafiobci.userapi.exceptions;

public class ServiceCallException extends RuntimeException {
    public ServiceCallException(String message) {
        super(message);
    }
}
