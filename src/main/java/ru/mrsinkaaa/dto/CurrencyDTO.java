package ru.mrsinkaaa.dto;

public class CurrencyDTO {

    private final String code;
    private final String name;
    private final String sign;

    public CurrencyDTO(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
