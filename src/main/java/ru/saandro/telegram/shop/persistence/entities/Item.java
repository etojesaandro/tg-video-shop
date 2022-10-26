package ru.saandro.telegram.shop.persistence.entities;

import java.io.IOException;
import java.nio.file.Path;

import ru.saandro.telegram.shop.core.*;

public interface Item {

    void sendPreviews(ShopBot bot, long chatId);

    void sendContent(ShopBot bot, long chatId);

    void store() throws ShopBotException;

    Path getContentPath();

    Path getPreviewPath();
}
