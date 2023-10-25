package ru.mrsinkaaa.exceptions;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException() {
        super("Input data is invalid");
    }
}
