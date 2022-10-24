package ru.saandro.telegram.shop.core;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

import ru.saandro.telegram.shop.conf.BotCommands;
import ru.saandro.telegram.shop.conf.BotConfiguration;
import ru.saandro.telegram.shop.conf.SetMyCommandsImpl;
import ru.saandro.telegram.shop.request.ResponseGenerator;
import ru.saandro.telegram.shop.session.UserSession;

public class ShopBotImpl implements ShopBot {

    private final TelegramBot telegramBot;

    private final ConcurrentHashMap<Long, UserSession> userSessionMap = new ConcurrentHashMap<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public ShopBotImpl(BotConfiguration botConfiguration) {
        this(botConfiguration.getToken());
    }

    public ShopBotImpl(String botToken) {
        telegramBot = new TelegramBot(botToken);
    }

    @Override
    public void start() {
        telegramBot.execute(new SetMyCommandsImpl(BotCommands.values()));
        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                UpdateWrapper updateWrapper = new UpdateWrapper(update);
                UserSession userSession = getOrCreateUserSession(updateWrapper);
                try {
                    userSession.processCommandAsync(updateWrapper);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        return telegramBot.execute(request);
    }

    private UserSession getOrCreateUserSession(UpdateWrapper updateWrapper) {

        Optional<Chat> chatOpt = updateWrapper.getChat();
        if (chatOpt.isEmpty()) {
            throw new RuntimeException("Chat is null");
        }

        Chat chat = chatOpt.get();
        UserSession userSession = userSessionMap.putIfAbsent(chat.id(), new UserSession(this, chat));
        if (userSession == null) {
            userSession = userSessionMap.get(chat.id());
            userSession.start();
        }
        return userSession;
    }
}
