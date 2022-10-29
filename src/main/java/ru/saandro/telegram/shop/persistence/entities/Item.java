package ru.saandro.telegram.shop.persistence.entities;

import java.io.*;
import java.nio.file.Path;

import ru.saandro.telegram.shop.core.*;

import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.*;

public interface Item {

    AbstractSendRequest<? extends AbstractSendRequest<?>> preparePreview(ShopBot bot, long chatId) throws IOException;

    AbstractSendRequest<? extends AbstractSendRequest<?>> prepareContent(ShopBot bot, long chatId) throws IOException;

    Long id();

    String title() throws IOException;

    String description() throws IOException;

    String author() throws IOException;

    Integer price() throws IOException;

    String previewPath() throws IOException;

    String contentPath() throws IOException;


}
