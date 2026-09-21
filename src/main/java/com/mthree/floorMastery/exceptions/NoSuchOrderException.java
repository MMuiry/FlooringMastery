package com.mthree.floorMastery.exceptions;

//throws exception when there's no order
public class NoSuchOrderException extends RuntimeException {
    public NoSuchOrderException(String message) {
        super(message);
    }
}
