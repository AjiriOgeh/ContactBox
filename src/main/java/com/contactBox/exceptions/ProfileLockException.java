package com.contactBox.exceptions;

public class ProfileLockException extends RuntimeException{
    public ProfileLockException(String message) {
        super(message);
    }
}
