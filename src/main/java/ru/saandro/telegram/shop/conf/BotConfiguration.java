package ru.saandro.telegram.shop.conf;

import java.nio.file.Path;

public interface BotConfiguration {
    String getToken();

    String getDatabaseUrl();

    String getDatabaseUser();

    String getDatabasePassword();

    Path getPreviewStoragePath();

    Path getContentStoragePath();
}
