package ru.saandro.telegram.shop.controller;

import java.nio.file.Path;
import javax.sql.DataSource;

import ru.saandro.telegram.shop.conf.BotConfiguration;
import ru.saandro.telegram.shop.core.*;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;

public class ThickItemBuilder {

    private final BotConfiguration configuration;

    private String title;
    private String description;
    private int price;
    private ContentFile preview;
    private ContentFile content;
    private VideoGenres genre;

    public ThickItemBuilder(BotConfiguration configuration) {
        this.configuration = configuration;
    }

    public void title(String title) {

        this.title = title;
    }

    public void description(String text) {
        description = text;
    }

    public void price(int price) {
        this.price = price;
    }

    public void preview(ContentFile preview) {
        this.preview = preview;
    }

    public void content(ContentFile content) {
        this.content = content;
    }

    public Item build(ShopBot provider, String userName, SimpleTelegramLogger logger) {
        return new ThickItem(
                new PgItem(provider, null, title, description, userName, genre, price, getPreviewPath(), getContentPath()), logger,
                preview, content);
    }

    private Path getPreviewPath() {
        return configuration.getPreviewStoragePath().resolve(genre.name).resolve(preview.file.fileUniqueId() + "." + preview.getExtention());
    }

    private Path getContentPath() {
        return configuration.getContentStoragePath().resolve(genre.name).resolve(content.file.fileUniqueId() + "." + content.getExtention());
    }

    public void genre(VideoGenres genre) {
        this.genre = genre;
    }
}
