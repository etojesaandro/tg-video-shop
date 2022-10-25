package ru.saandro.telegram.shop.dao;

import java.io.IOException;
import java.nio.file.Path;

import ru.saandro.telegram.shop.core.ShopBot;

public interface Item {

    void sendPreviews(ShopBot bot, long chatId);

    void sendContent(ShopBot bot, long chatId);

    void store() throws IOException;

    Path getContentPath();

    Path getPreviewPath();
}
