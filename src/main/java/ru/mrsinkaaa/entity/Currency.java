package ru.mrsinkaaa.entity;

public class Currency {

    private Long id;
    private final String code;
    private final String fullName;
    private final String sign;

    public Currency(Long id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Currency(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return "Currency{" +
              "code='" + code + '\'' +
              ", fullName='" + fullName + '\'' +
              ", sign='" + sign + '\'' +
              '}';
    }
}
