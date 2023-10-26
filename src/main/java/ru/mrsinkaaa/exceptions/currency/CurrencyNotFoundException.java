package ru.mrsinkaaa.exceptions.currency;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class CurrencyNotFoundException extends ApplicationException {
    public CurrencyNotFoundException() {
        super(ErrorMessage.CURRENCY_NOT_FOUND);
    }
}
