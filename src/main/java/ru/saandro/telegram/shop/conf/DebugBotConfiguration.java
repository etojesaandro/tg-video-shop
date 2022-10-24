package ru.saandro.telegram.shop.conf;

public class DebugBotConfiguration implements BotConfiguration {

    public static final String DEBUG_HTTP_TOKEN = "5781804964:AAHzpi5rwjl2eGNtOqUbh48Zo33QFN43yW8";

    @Override
    public String getToken() {
        return DEBUG_HTTP_TOKEN;
    }
}
