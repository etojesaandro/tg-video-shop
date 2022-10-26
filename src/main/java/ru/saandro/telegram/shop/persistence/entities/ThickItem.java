package ru.saandro.telegram.shop.persistence.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import ru.saandro.telegram.shop.controller.ContentFile;
import ru.saandro.telegram.shop.core.*;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;

import com.pengrad.telegrambot.request.*;

public class ThickItem implements Item {

    private final Item origin;
    private final SimpleTelegramLogger logger;
    private final ContentFile preview;
    private final ContentFile content;

    public ThickItem(Item item, SimpleTelegramLogger logger, ContentFile preview, ContentFile content) {
        this.origin = item;
        this.logger = logger;
        this.preview = preview;
        this.content = content;
    }

    public ThickItem(Item item, SimpleTelegramLogger logger) {
        this.origin = item;
        this.logger = logger;
        preview = loadPreview();
        content = loadContent();
    }

    private ContentFile loadContent() {
        return ContentFile.of(origin.getContentPath());
    }

    private ContentFile loadPreview() {
        return ContentFile.of(origin.getPreviewPath());

    }

    @Override
    public void sendPreviews(ShopBot bot, long chatId) {
        if (Files.exists(origin.getPreviewPath())) {
            SendVideo sendVideo = new SendVideo(chatId, origin.getPreviewPath().toFile());
            bot.execute(sendVideo);
        }
    }

    @Override
    public void sendContent(ShopBot bot, long chatId) {
        if (Files.exists(origin.getContentPath())) {
            SendVideo sendVideo = new SendVideo(chatId, origin.getContentPath().toFile());
            bot.execute(sendVideo);
        }
    }

    @Override
    public void store() throws ShopBotException {
        origin.store();
        try {
            saveFile(origin.getPreviewPath(), preview);
        } catch (IOException e) {
            throw new ShopBotException("Unable to save the preview", e);
        }
        try {
            saveFile(origin.getContentPath(), content);
        } catch (IOException e) {
            throw new ShopBotException("Unable to save the content", e);
        }
        origin.getContentPath();
    }

    private void saveFile(Path path, ContentFile contentFile) throws IOException {
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, contentFile.data);
    }

    @Override
    public Path getContentPath() {
        return origin.getContentPath();
    }

    @Override
    public Path getPreviewPath() {
        return origin.getPreviewPath();
    }
}
