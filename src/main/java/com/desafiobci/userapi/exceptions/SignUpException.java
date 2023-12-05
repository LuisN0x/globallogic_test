package com.desafiobci.userapi.exceptions;

public class SignUpException extends RuntimeException {
    public SignUpException(String message,Exception ex) { super(message,ex); }
}
