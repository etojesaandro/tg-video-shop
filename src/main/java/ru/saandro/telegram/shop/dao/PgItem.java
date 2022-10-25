package ru.saandro.telegram.shop.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sql.DataSource;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendVideo;

import ru.saandro.telegram.shop.controller.VideoGenres;
import ru.saandro.telegram.shop.core.ShopBot;

public class PgItem implements Item {

    private final DataSource source;
    private final Long id;
    private final String title;
    private final String description;
    private final String author;
    private final VideoGenres genre;
    private final int price;
    private final Path previewPath;
    private final Path contentPath;

    public PgItem(DataSource source, Long id, String title, String description, String author, VideoGenres genre, int price, Path previewPath, Path contentPath) {
        this.source = source;
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
        if (Files.exists(previewPath)) {
            SendVideo sendVideo = new SendVideo(chatId, previewPath.toFile());
            bot.execute(sendVideo);
        }
    }

    @Override
    public void store() throws IOException {

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
