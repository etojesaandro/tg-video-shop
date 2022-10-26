package ru.saandro.telegram.shop.core;

public class ShopBotException extends Exception {
    private String code;

    public ShopBotException(String message)
    {
        super(message);
    }

    public ShopBotException(String message, Throwable cause)
    {
        super(message != null ? message : String.valueOf(cause), cause);
    }
}
