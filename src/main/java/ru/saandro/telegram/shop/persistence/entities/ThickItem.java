package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.controller.*;
import ru.saandro.telegram.shop.core.*;
import ru.saandro.telegram.shop.logger.*;

import java.io.*;
import java.nio.file.*;

import com.pengrad.telegrambot.model.request.*;
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
        AbstractMultipartRequest<?> request = prepareRequest(chatId, origin.getPreviewPath());
        if (request != null)
        {
            prepareRequest(bot, request);
        }
    }

    @Override
    public void sendContent(ShopBot bot, long chatId) {
        AbstractMultipartRequest<?> request = prepareRequest(chatId, origin.getContentPath());
        if (request != null) {
            prepareRequest(bot, request);
        }
    }

    private void prepareRequest(ShopBot bot, AbstractMultipartRequest<?> request) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.addRow(prepareBuyButton(origin.getPrice()));
        request.replyMarkup(markupInline);

        bot.execute(request);
    }

    private InlineKeyboardButton prepareBuyButton(int price) {
        return new InlineKeyboardButton("Купить за " + price + " Jorge Coin").callbackData("buy_" + origin.getId());
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

    private AbstractMultipartRequest<?> prepareRequest(long chatId, Path previewPath) {
        if (Files.exists(previewPath)) {
            String fileExtension = com.google.common.io.Files.getFileExtension(previewPath.toString());
            if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("png")) {
                SendPhoto sendPhoto = new SendPhoto(chatId, previewPath.toFile());
                sendPhoto.caption(origin.getDescription());
                return sendPhoto;
            } else {
                SendVideo sendVideo = new SendVideo(chatId, previewPath.toFile());
                sendVideo.caption(origin.getDescription());
                return sendVideo;
            }
        }
        return null;
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

    @Override
    public int getPrice() {
        return origin.getPrice();
    }

    @Override
    public Long getId() {
        return origin.getId();
    }

    @Override
    public String getDescription() {
        return origin.getDescription();
    }
}
