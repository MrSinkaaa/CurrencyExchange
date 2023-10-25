package ru.mrsinkaaa.exceptions;

public class EmptyFormFieldException extends RuntimeException {

    public EmptyFormFieldException() {
        super("Required form field is missing");
    }
}
