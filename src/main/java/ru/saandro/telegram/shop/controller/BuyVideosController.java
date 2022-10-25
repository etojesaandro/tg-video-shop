package ru.saandro.telegram.shop.controller;

import java.util.Optional;

import com.pengrad.telegrambot.model.CallbackQuery;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.dao.ConstPgItems;
import ru.saandro.telegram.shop.dao.Item;
import ru.saandro.telegram.shop.dao.PgBotUser;
import ru.saandro.telegram.shop.session.UserSession;

public class BuyVideosController extends AbstractScreenController {

    public BuyVideosController(ShopBot bot, UserSession session, Long id) {
        super(bot, session, id);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        Optional<VideoGenres> parse = EnumWithDescription.parse(callbackQuery.data(), VideoGenres.class);
        if (parse.isEmpty()) {
            return;
        }
        switch (parse.get()) {
            case FOOT -> onFootVideos();
            case SCARFING -> onScarfingVideos();
            case ALL -> onAllVideos();
            case BACK -> {
                session.switchTo(BotScreens.HOME);
            }
        }
    }

    private void onAllVideos() {
        sendTheListOfItems(new ConstPgItems(bot.getDataSource(), bot.getLogger()).browseItemsByUserAndGenre(session.getUser().uid(), VideoGenres.ALL));
    }

    private void onScarfingVideos() {
        PgBotUser user = session.getUser();
        sendTheListOfItems(new ConstPgItems(bot.getDataSource(), bot.getLogger()).browseItemsByUserAndGenre(session.getUser().uid(), VideoGenres.SCARFING));
    }

    private void onFootVideos() {
        sendTheListOfItems(new ConstPgItems(bot.getDataSource(), bot.getLogger()).browseItemsByUserAndGenre(session.getUser().uid(), VideoGenres.FOOT));
    }

    private void sendTheListOfItems(Iterable<Item> items) {
        for (Item item : items) {
            item.sendPreviews(bot, chatId);
        }
    }

    @Override
    public void onStart() {
        prepareAndSendMenu("Чего желаете?", VideoGenres.class);
    }
}
