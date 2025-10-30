package ru.saandro.telegram.shop.controller;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.persistence.entities.BotUser;
import ru.saandro.telegram.shop.persistence.entities.CachedPgUsers;
import ru.saandro.telegram.shop.session.UserSession;

import java.io.IOException;
import java.util.Optional;

public class PromotionController extends AbstractScreenController {

    public PromotionController(ShopBot bot, UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
        session.switchTo(BotScreens.CONTROL_ROOM);
    }

    @Override
    public void processMessage(Message message) throws IOException {
        String name = message.text();
        cleanTheMessage(message.messageId());
        if (!name.startsWith("@")) {
            prepareAndSendMenu("Некорректное имя. Попробуйте ещё раз.", BackCommand.class);
            return;
        }
        Optional<? extends BotUser> user = new CachedPgUsers(bot.getSource()).findUser(name.substring(1));
        if (user.isPresent())
        {
            user.get().promote();
            prepareAndSendMenu(name + " теперь Администратор!", BackCommand.class);
        } else {
            prepareAndSendMenu("Пользователь " + name + " не найден! Ему требуется хотя бы раз запустить бота для регистрации.", BackCommand.class);
        }


    }

    @Override
    public void onStart() throws IOException {
        prepareAndSendMenu("Введите @name будущего Администратора.", BackCommand.class);
    }
}
