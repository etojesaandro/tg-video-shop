package ru.saandro.telegram.shop.controller;

import java.sql.*;
import java.util.Optional;
import java.util.logging.*;

import com.pengrad.telegrambot.model.CallbackQuery;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.session.UserSession;

public class BuyVideosController extends AbstractScreenController {

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
        try
        {
            sendTheListOfItems(new PgItems(bot, bot.getLogger()).browseItemsByGenre(Long.parseLong(data)));
        } catch (NumberFormatException e)
        {
            bot.getLogger().log(Level.WARNING, "Incorrect Genre id", e );
            prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
        }
    }

    private void sendTheListOfItems(Iterable<Item> items) {
        for (Item item : items) {
            new ThickItem(item, bot.getLogger()).sendPreviews(bot, chatId);
        }
    }

    @Override
    public void onStart() {
        try {
            Optional<? extends BotUser> user = new PgUsers(bot, bot.getLogger()).findUser(session.getUser().id());
            if (user.isPresent())
            {
                prepareAndSendMenu("Ваш баланс: " + user.get().balance() + ". Чего желаете? ", new PgGenres(bot).getAllGenres());
            }
        } catch (SQLException e) {
            bot.getLogger().log(Level.WARNING, "Unable to get Genres", e);
            prepareAndSendMenu("Сервис временно недоступен");
        }
    }
}
