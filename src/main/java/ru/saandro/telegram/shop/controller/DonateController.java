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
        session.switchTo(BotScreens.HOME);
    }

    @Override
    public void onStart() {
        prepareAndSendMenu("+79067657732 Сбербанк, в любое время \uD83D\uDE4F \uD83D\uDE4F.", BackCommand.class);
    }
}
