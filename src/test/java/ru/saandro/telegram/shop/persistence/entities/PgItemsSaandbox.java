package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.conf.*;
import ru.saandro.telegram.shop.core.*;

import java.nio.file.*;

class PgItemsSaandbox {

    private static final BotConfiguration CONFIG = new DefaultBotConfiguration();

    public static void main(String[] args) throws ShopBotException {
        PgSource pgSource = new PgSource(CONFIG.getDatabaseUrl(), CONFIG.getDatabaseUser(), CONFIG.getDatabasePassword());
        PgItem pgItem = new PgItem(() -> pgSource, null, "test", "test", "saandro", 1L,
                123, Paths.get("preview"), Paths.get("content"));
        pgItem.store();
    }
}