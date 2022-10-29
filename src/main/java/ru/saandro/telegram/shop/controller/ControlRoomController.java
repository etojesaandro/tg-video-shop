package ru.saandro.telegram.shop.controller;

import java.io.IOException;
import java.util.Optional;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public class ControlRoomController extends AbstractScreenController {

    public ControlRoomController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
        Optional<ControlRoomCommands> parse = EnumWithDescription.parse(callbackQuery.data(), ControlRoomCommands.class);
        if (parse.isEmpty()) {
            return;
        }
        switch (parse.get()) {
            case UPLOAD -> session.switchTo(BotScreens.UPLOAD_VIDEO);
            case PROCESS_GENRE -> session.switchTo(BotScreens.PROCESS_GENRE);
            case STATISTIC -> session.switchTo(BotScreens.STATISTIC);
            case PROMOTE -> session.switchTo(BotScreens.PROMOTE);
            case BACK -> session.switchTo(BotScreens.HOME);
            default -> throw new IllegalStateException("Unexpected value: " + parse.get());
        }
    }

    @Override
    public void onStart() throws IOException {
        prepareAndSendMenu("Добрый день, *Администратор*. Чем я могу вам помочь сегодня?", ControlRoomCommands.class);
    }
}
