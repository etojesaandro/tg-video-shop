package ru.saandro.telegram.shop.conf;

import java.nio.file.Path;

public class DebugBotConfiguration implements BotConfiguration {

    public static final String DEBUG_HTTP_TOKEN = "5781804964:AAHzpi5rwjl2eGNtOqUbh48Zo33QFN43yW8";

    @Override
    public String getToken() {
        return DEBUG_HTTP_TOKEN;
    }

    @Override
    public String getDatabaseUrl() {
        return "localhost:1433";
    }

    @Override
    public String getDatabaseUser() {
        return "admin";
    }

    @Override
    public String getDatabasePassword() {
        return "admin";
    }

    @Override
    public Path getPreviewStoragePath() {
        return Path.of("preview");
    }

    @Override
    public Path getContentStoragePath() {
        return Path.of("content");
    }

    @Override
    public void promoteAdmin(String name) {
    }
}
