package ru.saandro.telegram.shop.session;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;

import ru.saandro.telegram.shop.conf.BotCommands;
import ru.saandro.telegram.shop.controller.BotScreens;
import ru.saandro.telegram.shop.controller.BuyVideosController;
import ru.saandro.telegram.shop.controller.ControlRoomController;
import ru.saandro.telegram.shop.controller.DonateController;
import ru.saandro.telegram.shop.controller.HomeScreenController;
import ru.saandro.telegram.shop.controller.MyVideosController;
import ru.saandro.telegram.shop.controller.PromotionController;
import ru.saandro.telegram.shop.controller.StatisticController;
import ru.saandro.telegram.shop.controller.UploadVideoController;
import ru.saandro.telegram.shop.core.ScreenController;
import ru.saandro.telegram.shop.core.ShopBot;
import ru.saandro.telegram.shop.core.UpdateWrapper;
import ru.saandro.telegram.shop.persistence.entities.*;

public class UserSession extends Thread {

    private final ShopBot bot;

    private final BotUser user;

    private final long chatId;

    private final BlockingQueue<UpdateWrapper> commandQueue = new LinkedBlockingQueue<>();

    private volatile ScreenController currentController;

    public UserSession(ShopBot bot, BotUser user, long chatId) {
        this.chatId = chatId;
        this.user = user;
        this.bot = bot;
    }

    public void processCommandAsync(UpdateWrapper update) throws InterruptedException {
        commandQueue.put(update);
    }

    @Override
    public void run() {
        while (bot.isRunning()) {
            try {
                UpdateWrapper command = commandQueue.take();
                if (command.isMessage()) {
                    processMessage(command.update.message());
                } else if (command.isCallbackQuery()) {
                    processCallbackQuery(command.update.callbackQuery());
                }

            } catch (InterruptedException e) {
                bot.getLogger().log(Level.SEVERE, "User session was interrupted!", e);
            }
        }
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        if (currentController != null) {
            currentController.processCallback(callbackQuery);
        }
    }

    private void processMessage(Message message) {
        BotCommands botCommand = BotCommands.parse(message.text());
        if (botCommand == null && currentController != null) {
            currentController.processMessage(message);
            return;
        }
        switch (botCommand) {
            case START:
                switchTo(BotScreens.HOME);
                break;
            default:
                bot.getLogger().log(Level.SEVERE, "Unexpected value: " + botCommand);
        }
    }

    public void switchTo(BotScreens home) {
        switch (home) {
            case HOME -> currentController = new HomeScreenController(bot, this, chatId);
            case BUY_VIDEOS -> currentController = new BuyVideosController(bot, this, chatId);
            case MY_VIDEOS -> currentController = new MyVideosController(bot, this, chatId);
            case DONATE -> currentController = new DonateController(bot, this, chatId);

            case CONTROL_ROOM -> currentController = new ControlRoomController(bot, this, chatId);
            case UPLOAD_VIDEO -> currentController = new UploadVideoController(bot, this, chatId);
            case STATISTIC -> currentController = new StatisticController(bot, this, chatId);
            case PROMOTE -> currentController = new PromotionController(bot, this, chatId);
        }
        currentController.onStart();
    }

    public BotUser getUser() {
        return user;
    }
}
