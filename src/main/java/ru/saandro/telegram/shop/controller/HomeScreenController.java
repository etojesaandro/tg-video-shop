package ru.saandro.telegram.shop.controller;

import java.util.Objects;
import java.util.Optional;

import com.pengrad.telegrambot.model.CallbackQuery;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public final class HomeScreenController extends AbstractScreenController {

    public HomeScreenController(ShopBot bot,
                                UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        Optional<HomeScreenCommands> parse = EnumWithDescription.parse(callbackQuery.data(), HomeScreenCommands.class);
        if (parse.isEmpty()) {
            return;
        }
        switch (parse.get()) {
            case BUY_VIDEOS -> session.switchTo(BotScreens.BUY_VIDEOS);
            case MY_VIDEOS -> session.switchTo(BotScreens.MY_VIDEOS);
            case DONATE -> session.switchTo(BotScreens.DONATE);
            case CONTROL_ROOM -> session.switchTo(BotScreens.CONTROL_ROOM);

        }
    }

    @Override
    public void onStart() {
        prepareAndSendMenu("Впереди - нечто потрясающее!", HomeScreenCommands.class);
    }

    public ShopBot bot() {
        return bot;
    }

    @Override
    public String toString() {
        return "HomeScreenController[" +
                "bot=" + bot + ", " +
                "chatId=" + chatId + ']';
    }

}
