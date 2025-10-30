package ru.saandro.telegram.shop.message;

import ru.saandro.telegram.shop.controller.EnumWithDescription;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.persistence.entities.Markable;
import ru.saandro.telegram.shop.session.UserSession;

import java.io.IOException;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;

public class EditMessageProcessor extends AbstractMessageProcessor {

    public EditMessageProcessor(ShopBot bot, UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    public void sendMessage(Integer lastMessageId, String title, Iterable<? extends Markable> items) throws IOException {
        EditMessageText message = new EditMessageText(chatId, lastMessageId, title);
        message.parseMode(ParseMode.Markdown);
        message.replyMarkup(getInlineKeyboardMarkup(items));
        bot.execute(message);
    }

    public <E extends Enum<E> & EnumWithDescription> void sendMessage(Integer lastMessageId, String title, Class<E> enumClass) throws IOException {
        EditMessageText message = new EditMessageText(chatId, lastMessageId, title);
        message.parseMode(ParseMode.Markdown);
        message.replyMarkup(getInlineKeyboardMarkup(enumClass));
        bot.execute(message);
    }
}
