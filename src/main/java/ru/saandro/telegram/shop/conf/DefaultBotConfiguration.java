package ru.saandro.telegram.shop.conf;

import java.nio.file.Path;

public class DefaultBotConfiguration implements BotConfiguration {

    @Override
    public String getToken() {
        return "5781804964:AAHzpi5rwjl2eGNtOqUbh48Zo33QFN43yW8";
    }

    @Override
    public String getDatabaseUrl() {
        return "jdbc:postgresql://localhost:5432/shopbot";
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
}
