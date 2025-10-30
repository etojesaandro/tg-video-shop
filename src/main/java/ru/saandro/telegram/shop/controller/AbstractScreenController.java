package ru.saandro.telegram.shop.controller;

import ru.saandro.telegram.shop.core.ScreenController;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.message.EditMessageProcessor;
import ru.saandro.telegram.shop.message.SendMessageProcessor;
import ru.saandro.telegram.shop.persistence.entities.Markable;
import ru.saandro.telegram.shop.session.UserSession;

import java.io.IOException;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.DeleteMessage;

public abstract class AbstractScreenController implements ScreenController {

    protected final ShopBot bot;
    protected final UserSession session;
    protected final Long chatId;

    private final EditMessageProcessor editMessageProcessor;
    private final SendMessageProcessor sendMessageProcessor;

    protected AbstractScreenController(ShopBot bot, UserSession session, Long chatId) {
        this.bot = bot;
        this.session = session;
        this.chatId = chatId;
        editMessageProcessor = new EditMessageProcessor(bot, session, chatId);
        sendMessageProcessor = new SendMessageProcessor(bot, session, chatId);
    }

    protected void prepareAndSendMenu(String title) throws IOException {
        prepareAndSendMenu(title, BackCommand.class);
    }

    protected void cleanTheMessage(Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        bot.execute(deleteMessage);
    }

    protected void reportError() throws IOException {
        prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну или напишите мне, что я облажался хд.", BackCommand.class);
    }

    private void prepareAndSendInputRequest(String s, Class<BackCommand> backCommandClass) {

    }

    protected <E extends Enum<E> & EnumWithDescription> void prepareAndSendMenu(String title, Class<E> enumClass) throws IOException {
        Integer lastMessageId = session.getLastMessageId();

        if (lastMessageId != null) {
            editMessageProcessor.sendMessage(session.getLastMessageId(), title, enumClass);
        } else {
            Message message = sendMessageProcessor.sendMessage(title, enumClass);
            if (message != null)
            {
                session.updateLastMessageId(message.messageId());
            }
        }
    }

    protected <E extends Enum<E> & EnumWithDescription> void prepareAndSendMenu(String title, Iterable<? extends Markable> items) throws IOException {

        Integer lastMessageId = session.getLastMessageId();

        if (lastMessageId != null) {
            editMessageProcessor.sendMessage(session.getLastMessageId(), title, items);
        } else {
            Message message = sendMessageProcessor.sendMessage(title, items);
            if (message != null)
            {
                session.updateLastMessageId(message.messageId());
            }
        }
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
    }

    @Override
    public void processMessage(Message message) throws IOException {
        onStart();
    }

}
