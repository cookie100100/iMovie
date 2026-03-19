package org.example.imovie.entity;

public enum OrderStatus {
    New("PENDING"),
    PAID("PAID"),
    CANCEL("CANCEL");

    String message;

    OrderStatus(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
