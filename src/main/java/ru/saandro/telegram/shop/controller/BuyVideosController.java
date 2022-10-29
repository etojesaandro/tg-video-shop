package ru.saandro.telegram.shop.controller;

import ru.saandro.telegram.shop.core.*;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.session.*;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import org.jetbrains.annotations.*;

public class BuyVideosController extends AbstractScreenController {

    public static final String BOUGHT = "bought";
    private volatile BuyVideoState state = BuyVideoState.SELECT_GENRE;
    private Iterable<Item> currentItems;
    private Item itemToPurchase;
    private long currentGenre;

    public BuyVideosController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
        String data = callbackQuery.data();
        if (EnumWithDescription.parse(callbackQuery.data(), BackCommand.class).isPresent()) {
            session.switchTo(BotScreens.HOME);
            return;
        }
        if (state == BuyVideoState.SELECT_GENRE) {
            selectGenre(data);

        } else if (state == BuyVideoState.SELECT_VIDEO) {
            if (data.equals(BOUGHT))
            {
                return;
            }
            buyVideo(data);
        } else if (state == BuyVideoState.PURCHASE_CONFIRMATION) {

            Optional<ConfirmationCommands> confirmedOpt = EnumWithDescription.parse(data, ConfirmationCommands.class);
            if (confirmedOpt.isPresent()) {
                ConfirmationCommands confirmationCommands = confirmedOpt.get();
                switch (confirmationCommands) {
                    case YES -> {
                        try {
                            session.getUser().purchaseItem(itemToPurchase);
                            sendTheContent(itemToPurchase);
                        } catch (IOException e) {
                            bot.getLogger().log(Level.SEVERE, "Unable to purchase an Item", e);
                            prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
                            session.switchTo(BotScreens.HOME);
                        }
                    }
                    case NO -> {
                        state = BuyVideoState.SELECT_VIDEO;
                        selectGenre(currentGenre + "");
                    }
                }
            }
        }
    }

    private void buyVideo(String data) throws IOException {
        for (Item currentItem : currentItems) {
            if (currentItem.id().equals(Long.parseLong(data))) {
                if (session.getUser().balance() < currentItem.price()) {
                    prepareAndSendMenu("Извините, у вас недостаточно средств. Пополните кошелёк и возвращайтесь!");
                    selectGenre(currentGenre + "");
                    return;
                }

                itemToPurchase = currentItem;
                state = BuyVideoState.PURCHASE_CONFIRMATION;
                prepareAndSendMenu("Вы выбрали " + currentItem.title() + " за " + currentItem.price() + " JC. Приобрести?", ConfirmationCommands.class);

            }
        }
    }

    private void selectGenre(String data) {
        try {
            currentGenre = Long.parseLong(data);
            currentItems = new CachedPgItems(bot.getSource()).browseItemsByGenre(currentGenre);
            if (!currentItems.iterator().hasNext())
            {
                prepareAndSendMenu("Таких видео ещё нет! Выберите другой жанр..");
                return;
            }
            sendTheListOfItems(currentItems);
            state = BuyVideoState.SELECT_VIDEO;
        } catch (NumberFormatException e) {
            bot.getLogger().log(Level.WARNING, "Incorrect Genre id", e);
            prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
        } catch (Exception e) {
            prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
        }
    }

    private void sendTheListOfItems(Iterable<Item> items) {
        for (Item item : items) {
            try {
                AbstractSendRequest<? extends AbstractSendRequest<?>> request =
                        new ThickItem(item).preparePreview(bot, chatId);
                if (session.getUser().hasItem(item))
                {
                    sendRequest(bot, item, request, prepareAlreadyBoughtMarkups());
                    continue;
                }
                sendRequest(bot, item, request, prepareBuyMarkups(item));
            } catch (IOException e) {
                prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
            }
        }
    }

    private InlineKeyboardMarkup prepareAlreadyBoughtMarkups() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.addRow(new InlineKeyboardButton("Куплено").callbackData(BOUGHT));
        markupInline.addRow(createButton(BackCommand.BACK));
        return markupInline;
    }

    private void sendTheContent(Item item) throws IOException {

        prepareAndSendMenu("Всё прошло успешно. Ваш контент скоро появится. Также, все загруженные видео будут доступны в разделе \"Мои видео\". Приятного просмотра!");

        AbstractSendRequest<? extends AbstractSendRequest<?>> request =
                new ThickItem(item).prepareContent(bot, chatId);
        bot.execute(request);
    }

    private void sendRequest(ShopBot bot, Item item, AbstractSendRequest<? extends AbstractSendRequest<?>> request, InlineKeyboardMarkup markup) {
        request.replyMarkup(markup);
        bot.execute(request);
    }

    @NotNull
    private InlineKeyboardMarkup prepareBuyMarkups(Item item) throws IOException {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.addRow(prepareBuyButton(item));
        markupInline.addRow(createButton(BackCommand.BACK));
        return markupInline;
    }

    private InlineKeyboardButton prepareBuyButton(Item item) throws IOException {
        return new InlineKeyboardButton("Купить за " + item.price() + " JC").callbackData(item.id() + "");
    }

    @Override
    public void onStart() {
        try {
            Optional<? extends BotUser> user = new CachedPgUsers(bot.getSource()).findUser(session.getUser().id());
            if (user.isPresent()) {
                prepareAndSendMenu("Ваш баланс: " + user.get().balance() + " JC. Чего желаете? ",
                        new CachedPgGenres(bot.getSource()).getAllGenres());
            }
        } catch (Exception e) {
            bot.getLogger().log(Level.WARNING, "Unable to get Genres", e);
            prepareAndSendMenu("Сервис временно недоступен");
        }
    }
}
