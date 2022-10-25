package ru.saandro.telegram.shop.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import ru.saandro.telegram.shop.controller.ContentFile;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;

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

    @Override
    public void sendPreviews(ShopBot bot, long chatId) {
        origin.sendPreviews(bot, chatId);
    }

    @Override
    public void sendContent(ShopBot bot, long chatId) {
        origin.sendContent(bot, chatId);
    }

    @Override
    public void store() throws IOException {
        origin.store();
        try {
            saveFile(origin.getPreviewPath(), preview);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to save the preview", e);
        }
        try {
            saveFile(origin.getContentPath(), content);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Unable to save the content", e);
        }
        origin.getContentPath();
    }

    private void saveFile(Path path, ContentFile contentFile) throws IOException {
        if (!Files.exists(path.getParent()))
        {
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
