package ru.saandro.telegram.shop.controller;

import com.pengrad.telegrambot.model.CallbackQuery;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public class StatisticController extends AbstractScreenController {

    public StatisticController(ShopBot bot, UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        session.switchTo(BotScreens.CONTROL_ROOM);
    }

    @Override
    public void onStart() {
        prepareAndSendMenu("Куплено 100500 видосов на сумму 100500 JorgeCoins (1 JC = 1$).", BackCommand.class);
    }
}
