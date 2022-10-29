package ru.saandro.telegram.shop.controller;

import com.pengrad.telegrambot.model.CallbackQuery;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

import java.io.IOException;

public class StatisticController extends AbstractScreenController {

    public StatisticController(ShopBot bot, UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
        session.switchTo(BotScreens.CONTROL_ROOM);
    }

    @Override
    public void onStart() throws IOException {
        prepareAndSendMenu("Куплено *100500* видосов на сумму *100500 JorgeCoins*  *(1 JC = 1$)*.", BackCommand.class);
    }
}
