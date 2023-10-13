package ru.mrsinkaaa.dto;

public class MessageDTO {

    private String errorMessage;

    public MessageDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
