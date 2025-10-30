package ru.saandro.telegram.shop.message;

import ru.saandro.telegram.shop.controller.BackCommand;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.persistence.entities.Item;
import ru.saandro.telegram.shop.persistence.entities.ThickItem;
import ru.saandro.telegram.shop.session.UserSession;

import java.io.IOException;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AbstractSendRequest;

import static ru.saandro.telegram.shop.controller.BuyVideosController.BOUGHT;

public class ListOfItemsMessageProcessor extends AbstractMessageProcessor {

    public ListOfItemsMessageProcessor(ShopBot bot, UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    public void send(Iterable<Item> items) throws IOException {
        for (Item item : items) {
            AbstractSendRequest<? extends AbstractSendRequest<?>> request =
                    new ThickItem(item).preparePreview(bot, chatId);
            if (request == null)
            {
                return;
            }
            if (session.getUser().hasItem(item))
            {
                sendRequest(bot, request, prepareAlreadyBoughtMarkups());
                continue;
            }
            sendRequest(bot, request, prepareBuyMarkups(item));
        }
    }

    private void sendRequest(ShopBot bot, AbstractSendRequest<? extends AbstractSendRequest<?>> request, InlineKeyboardMarkup markup) {
        request.replyMarkup(markup);
        bot.execute(request);
    }

    private InlineKeyboardMarkup prepareAlreadyBoughtMarkups() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.addRow(new InlineKeyboardButton("Куплено").callbackData(BOUGHT));
        markupInline.addRow(createButton(BackCommand.BACK));
        return markupInline;
    }

    private InlineKeyboardMarkup prepareBuyMarkups(Item item) throws IOException {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.addRow(prepareBuyButton(item));
        markupInline.addRow(createButton(BackCommand.BACK));
        return markupInline;
    }

    private InlineKeyboardButton prepareBuyButton(Item item) throws IOException {
        return new InlineKeyboardButton("Купить за " + item.price() + " JC").callbackData(item.id() + "");
    }
}
