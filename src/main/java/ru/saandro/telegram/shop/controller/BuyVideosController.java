package ru.saandro.telegram.shop.controller;

import java.util.Optional;

import com.pengrad.telegrambot.model.CallbackQuery;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.session.UserSession;

public class BuyVideosController extends AbstractScreenController {

    public BuyVideosController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        Optional<VideoGenres> parse = EnumWithDescription.parse(callbackQuery.data(), VideoGenres.class);
        if (parse.isEmpty()) {
            return;
        }
        switch (parse.get()) {
            case FOOT -> onFootVideos();
            case SCARFING -> onScarfingVideos();
            case ALL -> onAllVideos();
            case BACK -> {
                session.switchTo(BotScreens.HOME);
            }
        }
    }

    private void onAllVideos() {
        sendTheListOfItems(new PgItems(bot, bot.getLogger()).browseItemsByGenre(VideoGenres.ALL));
    }

    private void onScarfingVideos() {
        BotUser user = session.getUser();
        sendTheListOfItems(new PgItems(bot, bot.getLogger()).browseItemsByGenre(VideoGenres.SCARFING));
    }

    private void onFootVideos() {
        sendTheListOfItems(new PgItems(bot, bot.getLogger()).browseItemsByGenre(VideoGenres.FOOT));
    }

    private void sendTheListOfItems(Iterable<Item> items) {
        for (Item item : items) {
            new ThickItem(item, bot.getLogger()).sendPreviews(bot, chatId);
        }
    }

    @Override
    public void onStart() {
        prepareAndSendMenu("Чего желаете? ", VideoGenres.class);
    }
}
