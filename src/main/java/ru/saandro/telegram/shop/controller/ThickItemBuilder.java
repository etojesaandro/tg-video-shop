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

    public ThickItemBuilder price(int price) {
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

    public Item build(PersistenceProvider provider, String userName, SimpleTelegramLogger logger) {
        return new ThickItem(
                new PgItem(provider, null, title, description, userName, genreId, price, getPreviewPath(), getContentPath()), logger,
                preview, content);
    }

    private Path getPreviewPath() {
        return configuration.getPreviewStoragePath().resolve(genreId + "").resolve(preview.file.fileUniqueId() + "." + preview.getExtention());
    }

    private Path getContentPath() {
        return configuration.getContentStoragePath().resolve(genreId + "").resolve(content.file.fileUniqueId() + "." + content.getExtention());
    }
}
