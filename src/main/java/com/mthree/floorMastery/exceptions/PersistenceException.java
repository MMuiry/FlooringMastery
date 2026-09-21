package com.mthree.floorMastery.exceptions;

//throws excpetions when no file
public class PersistenceException extends Exception {
    public PersistenceException(String message) {
        super(message);
    }
    public PersistenceException(String message, Throwable cause) {}
}
