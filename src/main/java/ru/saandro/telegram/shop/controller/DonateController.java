package ru.saandro.telegram.shop.controller;

import com.pengrad.telegrambot.model.CallbackQuery;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public class DonateController extends AbstractScreenController {

    public DonateController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {

    }

    @Override
    public void onStart() {

    }
}
