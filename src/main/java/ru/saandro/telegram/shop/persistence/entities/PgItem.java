package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.*;

import java.nio.file.*;
import java.sql.*;
import java.util.*;

import com.jcabi.jdbc.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;

public class PgItem implements Item {

    private final PersistenceProvider provider;
    private final Long id;
    private final String title;
    private final String description;
    private final String author;
    private final Long genre;
    private final int price;
    private final Path previewPath;
    private final Path contentPath;

    public PgItem(PersistenceProvider provider, Long id, String title, String description, String author, Long genre, int price, Path previewPath, Path contentPath) {
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

    public PgItem(PersistenceProvider provider, long key, PgItem other) {
        this.provider = provider;
        this.id = key;
        this.title = other.title;
        this.description = other.description;
        this.author = other.author;
        this.genre = other.genre;
        this.price = other.price;
        this.previewPath = other.previewPath;
        this.contentPath = other.contentPath;
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
            Optional<PgItem> result = new JdbcSession(provider.getSource())
                    .sql("INSERT INTO ITEM(TITLE, DESCRIPTION, AUTHOR, PRICE, PREVIEW_PATH, CONTENT_PATH) VALUES(?,?,?,?,?,?)")
                    .set(title).set(description).set(author).set(price).set(previewPath.toString()).set(contentPath.toString())
                    .insert(new ListOutcome<>(
                            rset -> new PgItem(provider, rset.getLong(1), this))).stream().findAny();
            if (result.isPresent()) {
                new JdbcSession(provider.getSource())
                        .sql("INSERT INTO ITEM_GENRE(ITEM_ID, GENRE_ID) VALUES(?,?)")
                        .set(result.get().id).set(genre).execute();
            }


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

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return prepareTheMessage();
    }

    private String prepareTheMessage() {
        return title + System.lineSeparator() + description;
    }
}
