package ru.mrsinkaaa.exceptions;

public class EmptyFormFieldException extends ApplicationException {


    public EmptyFormFieldException() {
        super(ErrorMessage.EMPTY_FORM_FIELD);
    }
}
