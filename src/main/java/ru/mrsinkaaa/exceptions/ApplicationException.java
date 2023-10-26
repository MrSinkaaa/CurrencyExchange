package ru.mrsinkaaa.exceptions;

public class ApplicationException extends RuntimeException {

    private final ErrorMessage error;

    public ApplicationException(ErrorMessage error) {
        this.error = error;
    }

    public ErrorMessage getError() {
        return error;
    }
}
