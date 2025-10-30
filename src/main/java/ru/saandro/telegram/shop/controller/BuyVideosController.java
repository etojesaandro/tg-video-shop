package ru.saandro.telegram.shop.controller;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.message.ListOfItemsMessageProcessor;
import ru.saandro.telegram.shop.persistence.entities.BotUser;
import ru.saandro.telegram.shop.persistence.entities.CachedPgGenres;
import ru.saandro.telegram.shop.persistence.entities.CachedPgItems;
import ru.saandro.telegram.shop.persistence.entities.CachedPgUsers;
import ru.saandro.telegram.shop.persistence.entities.Item;
import ru.saandro.telegram.shop.persistence.entities.ThickItem;
import ru.saandro.telegram.shop.session.UserSession;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.AbstractSendRequest;

public class BuyVideosController extends AbstractScreenController {

    public static final String BOUGHT = "bought";

    private volatile BuyVideoState state = BuyVideoState.SELECT_GENRE;
    private Iterable<Item> currentItems;
    private Item itemToPurchase;
    private long currentGenre;
    private boolean noVideosDetected;

    public BuyVideosController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) throws IOException {
        String data = callbackQuery.data();

        if (EnumWithDescription.parse(callbackQuery.data(), BackCommand.class).isPresent()) {
            if (noVideosDetected)
            {
                noVideosDetected = false;
                onStart();
                return;
            }
            if (state != BuyVideoState.SELECT_GENRE)
            {
                session.updateLastMessageId(null);
            }
            session.switchTo(BotScreens.HOME);
            return;
        }

        if (state == BuyVideoState.SELECT_GENRE) {
            selectGenre(data);

        } else if (state == BuyVideoState.SELECT_VIDEO) {
            if (data.equals(BOUGHT)) {
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
                Optional<? extends BotUser> optUser = new CachedPgUsers(bot.getSource()).findUser(session.getUser().id());
                if (optUser.isEmpty())
                {
                    return;
                }
                BotUser user = optUser.get();
                if (user.balance() < currentItem.price()) {
                    session.updateLastMessageId(null);
                    prepareAndSendMenu("Извините, у вас недостаточно средств. Пополните кошелёк и возвращайтесь!");
                    selectGenre(currentGenre + "");
                    return;
                }

                itemToPurchase = currentItem;
                state = BuyVideoState.PURCHASE_CONFIRMATION;
                session.updateLastMessageId(null);
                prepareAndSendMenu("Вы выбрали " + currentItem.title() + " за " + currentItem.price() + " JC. Приобрести?", ConfirmationCommands.class);

            }
        }
    }

    private void selectGenre(String data) throws IOException {
        try {
            currentGenre = Long.parseLong(data);
            currentItems = new CachedPgItems(bot.getSource()).browseItemsByGenre(currentGenre);
            if (!currentItems.iterator().hasNext()) {
                prepareAndSendMenu("Таких видео ещё нет! Выберите другой жанр..");
                noVideosDetected = true;
                return;
            }
            try {
                new ListOfItemsMessageProcessor(bot, session, chatId).send(currentItems);
            } catch (IOException e) {
                prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
            }
            state = BuyVideoState.SELECT_VIDEO;
        } catch (NumberFormatException e) {
            bot.getLogger().log(Level.WARNING, "Incorrect Genre id", e);
            prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
        } catch (Exception e) {
            prepareAndSendMenu("Произошла ошибка. Повторите позднее... Ну и напишите мне, что я облажался хд.");
        }
    }


    private void sendTheContent(Item item) throws IOException {

        prepareAndSendMenu("Всё прошло успешно. Ваш контент скоро появится. Также, все загруженные видео будут доступны в разделе \"Мои видео\". Приятного просмотра!");

        AbstractSendRequest<? extends AbstractSendRequest<?>> request =
                new ThickItem(item).prepareContent(bot, chatId);
        bot.execute(request);
    }

    @Override
    public void onStart() throws IOException {
        try {
            Optional<? extends BotUser> user = new CachedPgUsers(bot.getSource()).findUser(session.getUser().id());
            if (user.isPresent()) {
                prepareAndSendMenu("Ваш баланс: *" + user.get().balance() + " JC*. Чего желаете? ",
                        new CachedPgGenres(bot.getSource()).getAllGenres());
            }
        } catch (Exception e) {
            bot.getLogger().log(Level.WARNING, "Unable to get Genres", e);
            prepareAndSendMenu("Сервис временно недоступен");
        }
    }
}
