package ru.saandro.telegram.shop.controller;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.persistence.entities.CachedGenre;
import ru.saandro.telegram.shop.persistence.entities.CachedPgGenres;
import ru.saandro.telegram.shop.persistence.entities.Item;
import ru.saandro.telegram.shop.session.UserSession;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;

import com.google.common.io.ByteStreams;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Video;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;

import static ru.saandro.telegram.shop.controller.UploadState.CONFIRMATION;
import static ru.saandro.telegram.shop.controller.UploadState.CONTENT;
import static ru.saandro.telegram.shop.controller.UploadState.DESCRIPTION;
import static ru.saandro.telegram.shop.controller.UploadState.GENRE;
import static ru.saandro.telegram.shop.controller.UploadState.PREVIEW;
import static ru.saandro.telegram.shop.controller.UploadState.PRICE;
import static ru.saandro.telegram.shop.controller.UploadState.TITLE;

public class UploadVideoController extends AbstractScreenController {

    // https://api.telegram.org/file/bot<token>/<file_path>
    private static final String FILE_ADDRESS = "https://api.telegram.org/file/bot";

    private volatile UploadState uploadState = TITLE;

    private final ThickItemBuilder itemBuilder;

    public UploadVideoController(ShopBot bot, UserSession session, Long chatId) {
        super(bot, session, chatId);
        itemBuilder = new ThickItemBuilder(bot.getConfiguration());
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
        String data = callbackQuery.data();
        Optional<BackCommand> parse = EnumWithDescription.parse(data, BackCommand.class);
        if (parse.isPresent()) {
            uploadState = TITLE;
            session.switchTo(BotScreens.CONTROL_ROOM);
            return;
        }

        if (uploadState == CONFIRMATION) {
            Optional<ConfirmationCommands> confirmedOpt = EnumWithDescription.parse(data, ConfirmationCommands.class);
            if (confirmedOpt.isPresent()) {
                ConfirmationCommands confirmationCommands = confirmedOpt.get();
                switch (confirmationCommands) {
                    case YES -> {
                        try {
                            itemBuilder.buildAndStore(bot.getSource(), session.getUser().name(), bot.getLogger());
                        } catch (Exception e) {
                            reportError();
                            bot.getLogger().log(Level.WARNING, "Storage error", e);
                        }
                        prepareAndSendMenu("Видео успешно загружено и доступно для покупки!");
                    }
                    case NO -> prepareAndSendMenu("Загрузка отменена!", BackCommand.class);
                }
                session.switchTo(BotScreens.CONTROL_ROOM);
                return;
            }
        }

        if (uploadState == GENRE) {
            Optional<CachedGenre> genreOptional = new CachedPgGenres(bot.getSource()).getGenreById(Long.parseLong(data));
            if (genreOptional.isPresent()) {
                itemBuilder.genre(genreOptional.get());
                uploadState = PRICE;
                prepareAndSendMenu(itemBuilder.buildMessage() + "Введите стоимость видео.", BackCommand.class);
            }
        }
    }

    @Override
    public void processMessage(Message message) throws IOException {
        cleanTheMessage(message.messageId());
        switch (uploadState) {
            case TITLE -> {
                itemBuilder.title(message.text());
                uploadState = DESCRIPTION;

                prepareAndSendMenu(itemBuilder.buildMessage() + "Введите описание видео.", BackCommand.class);
            }
            case DESCRIPTION -> {
                itemBuilder.description(message.text());
                uploadState = GENRE;
                try {
                    prepareAndSendMenu(itemBuilder.buildMessage() + "Выберите жанр видео.", new CachedPgGenres(bot.getSource()).getAllGenres());
                } catch (Exception e) {
                    bot.getLogger().log(Level.WARNING, "Unable to get Genres");
                    reportError();
                }
            }

            case PRICE -> {
                String priceString = message.text();
                try {
                    int price = Integer.parseInt(priceString);
                    itemBuilder.price(price);
                    uploadState = PREVIEW;
                    prepareAndSendMenu(itemBuilder.buildMessage() + "Загрузите превью файл в виде изображения или видео.", BackCommand.class);
                } catch (NumberFormatException e) {
                    prepareAndSendMenu(itemBuilder.buildMessage() +"Некорректная стоимость. Введите ещё раз: ", BackCommand.class);
                }

            }
            case PREVIEW -> {
                try {
                    ContentFile preview = tryToReadFile(message);
                    if (preview.isInvalid()) {
                        return;
                    }
                    itemBuilder.preview(preview);
                } catch (IOException e) {
                    reportError();
                    bot.getLogger().log(Level.WARNING, "Preview loading error", e);
                    return;
                }
                uploadState = CONTENT;
                prepareAndSendMenu(itemBuilder.buildMessage() + "Загрузите контент.", BackCommand.class);
            }
            case CONTENT -> {
                try {
                    ContentFile content = tryToReadFile(message);
                    if (content.isInvalid()) {
                        return;
                    }
                    itemBuilder.content(content);
                } catch (IOException e) {
                    reportError();
                    bot.getLogger().log(Level.WARNING, "Content loading error", e);
                    return;
                }

                uploadState = CONFIRMATION;
                prepareAndSendMenu(itemBuilder.buildMessage() + "Всё готово! Подтверждаем загрузку?.", ConfirmationCommands.class);
            }
        }
    }

    private ContentFile tryToReadFile(Message message) throws IOException {
        GetFile getFile;
        Video video = message.video();
        if (message.photo() != null) {
            PhotoSize[] photo = message.photo();
            getFile = new GetFile(photo[photo.length - 1].fileId());
        } else if (message.video() != null) {
            getFile = new GetFile(video.fileId());
        } else {
            prepareAndSendMenu("Некорректный файл.");
            return ContentFile.INVALID;
        }

        GetFileResponse execute = bot.execute(getFile);
        File file = execute.file();
        String fileUrl = FILE_ADDRESS + bot.getToken() + "/" + file.filePath();
        byte[] bytes = downloadFile(fileUrl, file.fileSize());
        return new ContentFile(file.fileUniqueId(), file.filePath(), bytes);
    }

    private byte[] downloadFile(String address, long fileSize) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(address).openStream());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int) fileSize)) {
            ByteStreams.copy(in, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }


    @Override
    public void onStart() throws IOException {
        uploadState = TITLE;
        prepareAndSendMenu("Введите название видео.", BackCommand.class);
    }
}
