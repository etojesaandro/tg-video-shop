package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.controller.*;
import ru.saandro.telegram.shop.core.*;

import java.io.*;
import java.nio.file.*;

import com.pengrad.telegrambot.request.*;

public class ThickItem implements Item, ItemSender {

    private final Item origin;

    public ThickItem(Item item, ContentFile preview, ContentFile content) {
        this.origin = item;
    }

    public ThickItem(Item item) {
        this.origin = item;
    }

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> preparePreview(ShopBot bot, long chatId) throws IOException {
        return prepareRequest(chatId, Paths.get(origin.previewPath()));
    }

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> prepareContent(ShopBot bot, long chatId) throws IOException {
        return prepareRequest(chatId, Paths.get(origin.contentPath()));
    }

    private AbstractMultipartRequest<?> prepareRequest(long chatId, Path previewPath) throws IOException {
        if (Files.exists(previewPath)) {
            String fileExtension = com.google.common.io.Files.getFileExtension(previewPath.toString());
            if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("png")) {
                SendPhoto sendPhoto = new SendPhoto(chatId, previewPath.toFile());
                sendPhoto.caption(origin.description());
                return sendPhoto;
            } else {
                SendVideo sendVideo = new SendVideo(chatId, previewPath.toFile());
                sendVideo.caption(origin.description());
                return sendVideo;
            }
        }
        return null;
    }

    @Override
    public Long id() {
        return origin.id();
    }

    @Override
    public String title() throws IOException {
        return origin.title();
    }

    @Override
    public String description() throws IOException {
        return origin.description();
    }

    @Override
    public String author() throws IOException {
        return origin.author();
    }

    @Override
    public Integer price() throws IOException {
        return origin.price();
    }

    @Override
    public String contentPath() throws IOException {
        return origin.contentPath();
    }

    @Override
    public Genre genre() throws IOException {
        return origin.genre();
    }

    @Override
    public String previewPath() throws IOException {
        return origin.previewPath();
    }
}
