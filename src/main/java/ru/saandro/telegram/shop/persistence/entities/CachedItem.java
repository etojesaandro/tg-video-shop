package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.*;

import java.io.*;

import com.pengrad.telegrambot.request.*;

public class CachedItem implements Item {

    private final Item origin;
    private final String title;
    private final String description;
    private final String author;
    private final Integer price;
    private final String previewPath;
    private final String contentPath;
    private final Genre genre;

    public CachedItem(Item item, String title, String description, String author, Integer price, String previewPath, String contentPath, Genre genre) {
        this.origin = item;
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
        this.previewPath = previewPath;
        this.contentPath = contentPath;
        this.genre = genre;
    }

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> preparePreview(ShopBot bot, long chatId) throws IOException {
        return null;
    }

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> prepareContent(ShopBot bot, long chatId) throws IOException {
        return null;
    }

    @Override
    public Long id() {
        return origin.id();
    }

    @Override
    public String title() throws IOException {
        return title;
    }

    @Override
    public String description() throws IOException {
        return description;
    }

    @Override
    public String author() throws IOException {
        return author;
    }

    @Override
    public Integer price() throws IOException {
        return price;
    }

    @Override
    public String previewPath() throws IOException {
        return previewPath;
    }

    @Override
    public String contentPath() throws IOException {
        return contentPath;
    }

    @Override
    public Genre genre() throws IOException {
        return genre;
    }
}
