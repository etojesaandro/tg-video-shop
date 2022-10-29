package ru.saandro.telegram.shop.controller;

import java.io.IOException;
import java.nio.file.Path;

import ru.saandro.telegram.shop.conf.BotConfiguration;
import ru.saandro.telegram.shop.core.*;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;

import javax.sql.DataSource;

public class ThickItemBuilder {

    private final BotConfiguration configuration;

    private String title;
    private String description;
    private Integer price;
    private ContentFile preview;
    private ContentFile content;
    private Long genreId;

    public ThickItemBuilder(BotConfiguration configuration) {
        this.configuration = configuration;
    }

    public ThickItemBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ThickItemBuilder description(String text) {
        description = text;
        return this;
    }

    public ThickItemBuilder genre(Long genreId) {
        this.genreId = genreId;
        return this;
    }

    public ThickItemBuilder price(Integer price) {
        this.price = price;
        return this;
    }

    public ThickItemBuilder preview(ContentFile preview) {
        this.preview = preview;
        return this;
    }

    public ThickItemBuilder content(ContentFile content) {
        this.content = content;
        return this;
    }

    public Item buildAndStore(DataSource dataSource, String userName, SimpleTelegramLogger logger) throws IOException {
        return new ThickItem(new CachedPgItems(dataSource).add(title, description, userName, price, getPreviewPath(), getContentPath()));
    }

    private String getPreviewPath() {
        return configuration.getPreviewStoragePath().resolve(genreId + "").resolve(preview.file.fileUniqueId() + "." + preview.getExtention()).toString();
    }

    private String getContentPath() {
        return configuration.getContentStoragePath().resolve(genreId + "").resolve(content.file.fileUniqueId() + "." + content.getExtention()).toString();
    }
}
