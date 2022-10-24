package ru.saandro.telegram.shop.controller;

import java.util.EnumSet;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import ru.saandro.telegram.shop.core.ScreenController;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public abstract class AbstractScreenController implements ScreenController {

    protected final ShopBot bot;
    protected UserSession session;
    protected final Long chatId;

    protected AbstractScreenController(ShopBot bot, UserSession session, Long id) {
        this.bot = bot;
        this.session = session;
        this.chatId = id;
    }

    protected <E extends Enum<E> & EnumWithDescription> void prepareAndSendMenu(String title, Class<E> enumClass) {
        EnumSet<E> es = EnumSet.allOf(enumClass);
        SendMessage message = new SendMessage(chatId, title);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        for (EnumWithDescription e : es) {
            markupInline.addRow(new InlineKeyboardButton(e.getDescription()).callbackData(e.getName()));
        }
        message.replyMarkup(markupInline);
        bot.execute(message);
    }

}
