package ru.saandro.telegram.shop.controller;

import ru.saandro.telegram.shop.core.*;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.session.*;

import java.io.IOException;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.*;

public class MyVideosController extends AbstractScreenController {

    public MyVideosController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
        session.switchTo(BotScreens.HOME);
    }

    @Override
    public void onStart() throws IOException {
        try {
            Iterable<Item> purchasedItems = new CachedPgItems(bot.getSource()).getPurchasedItemsByUser(session.getUser().id());
            if (!purchasedItems.iterator().hasNext()) {
                prepareAndSendMenu("Вы ещё ничего не купили у нас? Возвращайся, когда будут деньги, приятель.", BackCommand.class);
                return;
            }
            for (Item item : purchasedItems) {
                ThickItem thickItem = new ThickItem(item);
                AbstractSendRequest<? extends AbstractSendRequest<?>> request = thickItem.prepareContent(bot, chatId);
                bot.execute(request);
            }
            prepareAndSendMenu("Приятного просмотра!", BackCommand.class);
        } catch (Exception e) {
            prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
        }
    }
}
