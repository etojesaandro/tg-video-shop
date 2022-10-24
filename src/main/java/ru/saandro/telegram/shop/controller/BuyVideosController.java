package ru.saandro.telegram.shop.controller;

import java.util.Optional;

import com.pengrad.telegrambot.model.CallbackQuery;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public class BuyVideosController extends AbstractScreenController {

    public BuyVideosController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        Optional<VideoGenres> parse = VideoGenres.parse(callbackQuery.data());
        if (parse.isEmpty()) {
            return;
        }
        switch (parse.get()) {
            case FOOT -> onFootVideos();
            case SCARFING -> onScarfingVideos();
            case ALL -> onAllVideos();
        }
    }

    private void onAllVideos() {

    }

    private void onScarfingVideos() {

    }

    private void onFootVideos() {

    }

    @Override
    public void onStart() {
        prepareAndSendMenu("Чего желаете?", VideoGenres.class);
    }
}
