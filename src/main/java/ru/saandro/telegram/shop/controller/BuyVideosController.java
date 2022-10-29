package ru.saandro.telegram.shop.controller;

import ru.saandro.telegram.shop.core.*;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.session.*;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;

public class BuyVideosController extends AbstractScreenController {

    private volatile BuyVideoState state = BuyVideoState.SELECT_GENRE;
    private Iterable<Item> currentItems;
    private Item itemToPurchase;
    private long currentGenre;

    public BuyVideosController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        String data = callbackQuery.data();
        if (EnumWithDescription.parse(callbackQuery.data(), BackCommand.class).isPresent()) {
            session.switchTo(BotScreens.HOME);
            return;
        }
        if (state == BuyVideoState.SELECT_GENRE) {
            selectGenre(data);

        } else if (state == BuyVideoState.SELECT_VIDEO) {
            buyVideo(data);
        } else if (state == BuyVideoState.PURCHASE_CONFIRMATION) {

            Optional<ConfirmationCommands> confirmedOpt = EnumWithDescription.parse(data, ConfirmationCommands.class);
            if (confirmedOpt.isPresent()) {
                ConfirmationCommands confirmationCommands = confirmedOpt.get();
                switch (confirmationCommands) {
                    case YES -> {
                        session.getUser().purchaseItem(itemToPurchase);
                        return;
                    }
                    case NO -> {
                        state = BuyVideoState.SELECT_VIDEO;
                        selectGenre(currentGenre + "");
                    }
                }
                return;
            }
        }
    }

    private void buyVideo(String data) {
        for (Item currentItem : currentItems) {
            if (currentItem.getId().equals(Long.parseLong(data))) {
                if (session.getUser().balance() < currentItem.getPrice()) {
                    prepareAndSendMenu("Извините, у вас недостаточно средств. Пополните кошелёк и возвращайтесь!");
                    selectGenre(currentGenre + "");
                    return;
                }

                itemToPurchase = currentItem;
                state = BuyVideoState.PURCHASE_CONFIRMATION;
                prepareAndSendMenu("Вы выбрали " + currentItem.getTitle() + " за " + currentItem.getPrice() + " JC. Приобрести?", ConfirmationCommands.class);

            }
        }
    }

    private void selectGenre(String data) {
        try {
            currentGenre = Long.parseLong(data);
            currentItems = new PgItems(bot, bot.getLogger()).browseItemsByGenre(currentGenre);
            sendTheListOfItems(currentItems);
            state = BuyVideoState.SELECT_VIDEO;
        } catch (NumberFormatException e) {
            bot.getLogger().log(Level.WARNING, "Incorrect Genre id", e);
            prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
        }
    }

    private void sendTheListOfItems(Iterable<Item> items) {
        for (Item item : items) {
            AbstractSendRequest<? extends AbstractSendRequest<?>> request =
                    new ThickItem(item, bot.getLogger()).preparePreview(bot, chatId);
            sendRequest(bot, item, request);
        }
    }

    private void sendRequest(ShopBot bot, Item item, AbstractSendRequest<? extends AbstractSendRequest<?>> request) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.addRow(prepareBuyButton(item));
        markupInline.addRow(createButton(BackCommand.BACK));
        request.replyMarkup(markupInline);
        bot.execute(request);
    }

    private InlineKeyboardButton prepareBuyButton(Item item) {
        return new InlineKeyboardButton("Купить за " + item.getPrice() + " JC").callbackData(item.getId() + "");
    }

    @Override
    public void onStart() {
        try {
            Optional<? extends BotUser> user = new PgUsers(bot, bot.getLogger()).findUser(session.getUser().id());
            if (user.isPresent()) {
                prepareAndSendMenu("Ваш баланс: " + user.get().balance() + " JC. Чего желаете? ", new PgGenres(bot).getAllGenres());
            }
        } catch (SQLException e) {
            bot.getLogger().log(Level.WARNING, "Unable to get Genres", e);
            prepareAndSendMenu("Сервис временно недоступен");
        }
    }
}
