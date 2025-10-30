package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.controller.ContentFile;
import ru.saandro.telegram.shop.core.ShopBot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.pengrad.telegrambot.request.AbstractMultipartRequest;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;

public class ThickItem implements Item, ItemSender {

    private final Item origin;
    private final ContentFile preview;
    private final ContentFile content;

    public ThickItem(Item item, ContentFile preview, ContentFile content) {
        this.origin = item;
        this.preview = preview;
        this.content = content;
    }

    public ThickItem(Item item) {
        this.origin = item;
        preview = null;
        content = null;
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

    public void store() throws IOException {
        if (preview != null)
        {
            Files.write(Files.createDirectories(Paths.get(origin.previewPath()).getParent()).resolve(preview.filePath), preview.data);
        }
        if (content != null)
        {
            Files.write(Files.createDirectories(Paths.get(origin.contentPath()).getParent()).resolve(content.filePath), content.data);
        }
    }
}
