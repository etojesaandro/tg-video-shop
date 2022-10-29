package ru.saandro.telegram.shop.controller;

import java.io.*;
import java.util.EnumSet;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import ru.saandro.telegram.shop.core.ScreenController;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.session.UserSession;

public abstract class AbstractScreenController implements ScreenController {

    protected final ShopBot bot;
    protected UserSession session;
    protected final Long chatId;

    protected AbstractScreenController(ShopBot bot, UserSession session, Long chatId) {
        this.bot = bot;
        this.session = session;
        this.chatId = chatId;
    }

    protected void prepareAndSendMenu(String title) {
        SendMessage message = new SendMessage(chatId, title);
        bot.execute(message);
    }

    protected <E extends Enum<E> & EnumWithDescription> void prepareAndSendMenu(String title, Class<E> enumClass) throws IOException {
        EnumSet<E> es = EnumSet.allOf(enumClass);
        SendMessage message = new SendMessage(chatId, title);
        message.parseMode(ParseMode.Markdown);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        for (EnumWithDescription e : es) {
            if (e.isAdmin() && !session.getUser().admin()) {
                continue;
            }
            markupInline.addRow(createButton(e));
        }
        message.replyMarkup(markupInline);
        bot.execute(message);
    }

    protected <E extends Enum<E> & EnumWithDescription> void prepareAndSendMenu(String title, Iterable<? extends Markable> items) throws IOException {
        SendMessage message = new SendMessage(chatId, title);
        message.parseMode(ParseMode.Markdown);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        for (Markable markable : items) {
            markupInline.addRow(new InlineKeyboardButton(markable.getMarkableDescription()).callbackData(markable.getMarkableName()));
        }
        markupInline.addRow(createButton(BackCommand.BACK));
        message.replyMarkup(markupInline);
        bot.execute(message);
    }

    protected InlineKeyboardButton createButton(EnumWithDescription e) {
        return new InlineKeyboardButton(e.getDescription()).callbackData(e.getName());
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
    }

    @Override
    public void processMessage(Message message) throws IOException {
        onStart();
    }

}
