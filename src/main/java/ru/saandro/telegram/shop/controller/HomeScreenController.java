package ru.saandro.telegram.shop.controller;

import java.util.Objects;
import java.util.Optional;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.session.UserSession;

public final class HomeScreenController extends AbstractScreenController {

    public HomeScreenController(ShopBot bot,
                                UserSession session, Long chatId) {
        super(bot, session, chatId);
    }

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        Optional<HomeScreenCommands> parse = HomeScreenCommands.parse(callbackQuery.data());
        if (parse.isEmpty()) {
            return;
        }
        switch (parse.get()) {
            case BUY_VIDEOS -> onBuyVideos();
            case MY_VIDEOS -> onMyVideos();
            case DONATE -> onDonate();
        }
    }

    @Override
    public void onStart() {
        prepareAndSendMenu("Впереди - нечто потрясающее!", HomeScreenCommands.class);
    }

    private void onBuyVideos() {
        session.switchTo(BotScreens.BUY_VIDEOS);
    }

    private void onMyVideos() {
        session.switchTo(BotScreens.MY_VIDEOS);
    }

    private void onDonate() {
        session.switchTo(BotScreens.DONATE);
    }

    public ShopBot bot() {
        return bot;
    }

    public Long chatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (HomeScreenController) obj;
        return Objects.equals(this.bot, that.bot) &&
                Objects.equals(this.chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bot, chatId);
    }

    @Override
    public String toString() {
        return "HomeScreenController[" +
                "bot=" + bot + ", " +
                "chatId=" + chatId + ']';
    }

}
