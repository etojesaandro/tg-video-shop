package ru.saandro.telegram.shop.message;

import ru.saandro.telegram.shop.controller.BackCommand;
import ru.saandro.telegram.shop.controller.EnumWithDescription;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.persistence.entities.Item;
import ru.saandro.telegram.shop.persistence.entities.Markable;
import ru.saandro.telegram.shop.session.UserSession;

import java.io.IOException;
import java.util.EnumSet;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMessageProcessor {

    protected final ShopBot bot;
    protected final UserSession session;
    protected final Long chatId;

    public AbstractMessageProcessor(ShopBot bot, UserSession session, Long chatId) {
        this.bot = bot;
        this.session = session;
        this.chatId = chatId;
    }

    protected InlineKeyboardMarkup getInlineKeyboardMarkup(Iterable<? extends Markable> items) throws IOException {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        for (Markable markable : items) {
            markupInline.addRow(new InlineKeyboardButton(markable.getMarkableDescription()).callbackData(markable.getMarkableName()));
        }
        markupInline.addRow(createButton(BackCommand.BACK));
        return markupInline;
    }

    protected <E extends Enum<E> & EnumWithDescription> InlineKeyboardMarkup getInlineKeyboardMarkup(Class<E> enumClass) throws IOException {
        EnumSet<E> es = EnumSet.allOf(enumClass);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        for (EnumWithDescription e : es) {
            if (e.isAdmin() && !session.getUser().admin()) {
                continue;
            }
            markupInline.addRow(createButton(e));
        }
        return markupInline;
    }

    protected InlineKeyboardButton createButton(EnumWithDescription e) {
        return new InlineKeyboardButton(e.getDescription()).callbackData(e.getName());
    }
}
