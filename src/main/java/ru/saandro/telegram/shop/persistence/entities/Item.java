package ru.saandro.telegram.shop.persistence.entities;

import java.nio.file.Path;

import ru.saandro.telegram.shop.core.*;

import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.*;

public interface Item {

    AbstractSendRequest<? extends AbstractSendRequest<?>> preparePreview(ShopBot bot, long chatId);

    AbstractSendRequest<? extends AbstractSendRequest<?>> sendContent(ShopBot bot, long chatId);

    void store() throws ShopBotException;

    Path getContentPath();

    Path getPreviewPath();

    int getPrice();

    Long getId();

    String getTitle();

    String getDescription();
}
