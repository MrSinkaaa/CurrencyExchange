package ru.mrsinkaaa.exceptions.currency;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class CurrencyAlreadyExistException extends ApplicationException {
    public CurrencyAlreadyExistException() {
        super(ErrorMessage.CURRENCY_ALREADY_EXISTS);
    }
}
