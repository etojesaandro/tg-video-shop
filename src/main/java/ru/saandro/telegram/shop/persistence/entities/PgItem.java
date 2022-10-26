package ru.saandro.telegram.shop.persistence.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.sql.DataSource;

import com.jcabi.jdbc.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendVideo;

import ru.saandro.telegram.shop.controller.VideoGenres;
import ru.saandro.telegram.shop.core.*;

public class PgItem implements Item {

    private final ShopBot provider;
    private final Long id;
    private final String title;
    private final String description;
    private final String author;
    private final VideoGenres genre;
    private final int price;
    private final Path previewPath;
    private final Path contentPath;

    public PgItem(ShopBot provider, Long id, String title, String description, String author, VideoGenres genre, int price, Path previewPath, Path contentPath) {
        this.provider = provider;
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.previewPath = previewPath;
        this.contentPath = contentPath;
    }

    public void sendPreviews(ShopBot bot, long chatId) {
        if (Files.exists(previewPath)) {
            SendVideo sendVideo = new SendVideo(chatId, previewPath.toFile());
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            markupInline.addRow(new InlineKeyboardButton("Купить за " + price).callbackData("buy_" + id));
            sendVideo.replyMarkup(markupInline);
            bot.execute(sendVideo);
        }
        SendMessage message = new SendMessage(chatId, prepareTheMessage());
        bot.execute(message);
    }

    public void sendContent(ShopBot bot, long chatId) {
    }

    @Override
    public void store() throws ShopBotException {
        try {
            DataSource source = provider.getSource();
            new JdbcSession(source)
                    .sql("INSERT INTO ITEM(ID, TITLE, DESCRIPTION, AUTHOR, PRICE, PREVIEW_PATH, CONTENT_PATH) VALUES(?,?,?,?,?,?,?)")
                    .set(id).set(title).set(description).set(author).set(price).set(previewPath).set(contentPath).execute();
        } catch (SQLException e) {
            throw new ShopBotException("Unable to create new user", e);
        }
    }

    @Override
    public Path getContentPath() {
        return contentPath;
    }

    @Override
    public Path getPreviewPath() {
        return previewPath;
    }

    private String prepareTheMessage() {
        return title + System.lineSeparator() + description;
    }
}
