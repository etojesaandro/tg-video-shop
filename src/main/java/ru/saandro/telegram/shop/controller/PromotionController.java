package ru.saandro.telegram.shop.controller;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public class PromotionController extends AbstractScreenController {

    public PromotionController(ShopBot bot, UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        session.switchTo(BotScreens.CONTROL_ROOM);
    }

    @Override
    public void processMessage(Message message) {
        String name = message.text();
        if (!name.startsWith("@")) {
            prepareAndSendMenu("Некорректное имя. Попробуйте ещё раз.", BackCommand.class);
            return;
        }
        bot.getConfiguration().promoteAdmin(name);
        prepareAndSendMenu(name + " теперь Администратор!", BackCommand.class);
    }

    @Override
    public void onStart() {
        prepareAndSendMenu("Введите @name будущего Администратора.", BackCommand.class);
    }
}
