package ru.mrsinkaaa.exceptions;

public class InvalidInputException extends ApplicationException {

    public InvalidInputException() {
        super(ErrorMessage.INVALID_DATA);
    }
}
