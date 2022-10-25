package ru.saandro.telegram.shop.controller;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;

import ru.saandro.telegram.shop.controller.AbstractScreenController;
import ru.saandro.telegram.shop.controller.ControlRoomCommands;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public class PromotionController extends AbstractScreenController {

    public PromotionController(ShopBot bot, UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    @Override
    public void processMessage(Message message) {
        // TODO Парсим имя будущего Админа
    }

    @Override
    public void onStart() {
        prepareAndSendMenu("Введите имя будущего Администратора.");
    }
}
