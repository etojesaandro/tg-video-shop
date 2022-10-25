package ru.saandro.telegram.shop.controller;

import com.pengrad.telegrambot.model.CallbackQuery;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public class MyVideosController extends AbstractScreenController {

    public MyVideosController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        session.switchTo(BotScreens.HOME);
    }

    @Override
    public void onStart() {
        prepareAndSendMenu("Вы ещё ничего не купили у нас? Возвращайся, когда будут деньги, приятель.", BackCommand.class);
    }
}
