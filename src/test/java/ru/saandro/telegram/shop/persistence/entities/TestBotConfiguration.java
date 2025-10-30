package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.conf.BotConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestBotConfiguration implements BotConfiguration {
    @Override
    public String getToken() {
        return "";
    }

    @Override
    public String getDatabaseUrl() {
        return "";
    }

    @Override
    public String getDatabaseUser() {
        return "";
    }

    @Override
    public String getDatabasePassword() {
        return "";
    }

    @Override
    public Path getPreviewStoragePath() {
        try {
            return Files.createTempDirectory("previews");
        } catch (IOException e) {
            return Paths.get("");
        }
    }

    @Override
    public Path getContentStoragePath() {
        try {
            return Files.createTempDirectory("contents");
        } catch (IOException e) {
            return Paths.get("");
        }
    }
}
