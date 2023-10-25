package ru.mrsinkaaa.service;

import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;

public class ValidationUtils {

    public static void validateCurrency(String code, String name, String sign) {
        if(isEmptyField(code, name, sign)) {
            throw new EmptyFormFieldException();
        } else if(!isValid(code, name, sign)) {
            throw new InvalidInputException();
        }
    }

    public static void validateExchangeRate(String base, String target, String rate) {
        if(isEmptyField(base, target, rate)) {
            throw new EmptyFormFieldException();
        } else if(!isValidArgs(base, target)) {
            throw new InvalidInputException();
        }
    }

    public static void validateExchange(String from, String to, String amount) {
        if(isEmptyField(from, to, amount)) {
            throw new EmptyFormFieldException();
        } else if(!isValidArgs(from, to)) {
            throw new InvalidInputException();
        }
    }

    private static boolean isValidArgs(String from, String to) {
        return false;
    }

    private static boolean isEmptyField(String arg1, String arg2, String arg3) {
        return arg1.isEmpty() || arg2.isEmpty() || arg3.isEmpty();
    }

    private static boolean isValid(String code, String name, String sign) {
        return false;
    }


}
