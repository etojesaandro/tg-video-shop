package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.ShopBot;

import java.io.IOException;

import com.pengrad.telegrambot.request.AbstractSendRequest;

public interface ItemSender {

    AbstractSendRequest<? extends AbstractSendRequest<?>> preparePreview(ShopBot bot, long chatId) throws IOException;

    AbstractSendRequest<? extends AbstractSendRequest<?>> prepareContent(ShopBot bot, long chatId) throws IOException;
}
