package ru.saandro.telegram.shop.message;

import ru.saandro.telegram.shop.controller.EnumWithDescription;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.persistence.entities.Markable;
import ru.saandro.telegram.shop.session.UserSession;

import java.io.IOException;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class SendMessageProcessor extends AbstractMessageProcessor {

    public SendMessageProcessor(ShopBot bot, UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    public Message sendMessage(String title, Iterable<? extends Markable> items) throws IOException {
        SendMessage message = new SendMessage(chatId, title);
        message.parseMode(ParseMode.Markdown);
        message.replyMarkup(getInlineKeyboardMarkup(items));
        return bot.execute(message).message();

    }

    public <E extends Enum<E> & EnumWithDescription> Message sendMessage(String title, Class<E> enumClass) throws IOException {
        SendMessage message = new SendMessage(chatId, title);
        message.parseMode(ParseMode.Markdown);
        message.replyMarkup(getInlineKeyboardMarkup(enumClass));
        return bot.execute(message).message();

    }
}
