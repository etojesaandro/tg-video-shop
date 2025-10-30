package ru.saandro.telegram.shop.controller;

import ru.saandro.telegram.shop.conf.BotConfiguration;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;
import ru.saandro.telegram.shop.persistence.entities.CachedPgItems;
import ru.saandro.telegram.shop.persistence.entities.Genre;
import ru.saandro.telegram.shop.persistence.entities.Item;
import ru.saandro.telegram.shop.persistence.entities.ThickItem;

import javax.sql.DataSource;
import java.io.IOException;

public class ThickItemBuilder {

    private final BotConfiguration configuration;

    private String title;
    private String description;
    private Integer price;
    private ContentFile preview;
    private ContentFile content;
    private Genre genre;

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

    public ThickItemBuilder genre(Genre genre) {
        this.genre = genre;
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
        ThickItem item = new ThickItem(new CachedPgItems(dataSource).add(title, description, userName, price, getPreviewPath(), getContentPath(), genre), preview, content);
        item.store();
        return item;
    }

    private String getPreviewPath() {
        return configuration.getPreviewStoragePath().resolve(genre.id() + "").resolve(preview.fileUniqueId + "." + preview.getExtention()).toString();
    }

    private String getContentPath() {
        return configuration.getContentStoragePath().resolve(genre.id() + "").resolve(content.fileUniqueId + "." + content.getExtention()).toString();
    }


    public String buildMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        if (title != null) {
            stringBuilder.append("*Название:* ").append(title).append(System.lineSeparator());
        }
        if (description != null) {
            stringBuilder.append("*Описание:* ").append(description).append(System.lineSeparator());
        }
        if (genre != null) {
            try {
                stringBuilder.append("*Жанр:* ").append(genre.name()).append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (price != null) {
            stringBuilder.append("*Стоимость:* ").append(price).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
