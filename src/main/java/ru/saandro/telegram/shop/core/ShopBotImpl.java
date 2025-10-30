package ru.saandro.telegram.shop.core;

import liquibase.exception.*;
import ru.saandro.telegram.shop.conf.*;
import ru.saandro.telegram.shop.logger.*;
import ru.saandro.telegram.shop.persistence.*;
import ru.saandro.telegram.shop.persistence.entities.*;
import ru.saandro.telegram.shop.session.*;

import javax.sql.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.pengrad.telegrambot.*;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.*;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

public class ShopBotImpl implements ShopBot {

    public static final String MASTER_CHANGE_LOG = "liquibase/master.liquibase.xml";

    private final TelegramBot telegramBot;

    private final SimpleTelegramLogger logger;

    private final BotConfiguration botConfiguration;

    private final ConcurrentHashMap<Long, UserSession> userSessionMap = new ConcurrentHashMap<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public ShopBotImpl(BotConfiguration botConfiguration, SimpleTelegramLogger simpleTelegramLogger) {
        this.telegramBot = new TelegramBot(botConfiguration.getToken());
        this.botConfiguration = botConfiguration;
        this.logger = simpleTelegramLogger;
    }

    @Override
    public void start() throws LiquibaseException, SQLException {
        LiquibaseHelper helper = new LiquibaseHelper(MASTER_CHANGE_LOG, getSource().getConnection(), "SCHEME");
        helper.migrate();
        telegramBot.execute(new SetMyCommandsImpl(BotCommands.values()));
        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                try {
                    UpdateWrapper updateWrapper = new UpdateWrapper(update);
                    UserSession userSession = getOrCreateUserSession(updateWrapper);
                    userSession.processCommandAsync(updateWrapper);
                } catch (Exception e) {
                    logger.log(WARNING, "Session creation fail", e);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        logger.log(INFO, "Бот запущен!");
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        return telegramBot.execute(request);
    }

    @Override
    public BotConfiguration getConfiguration() {
        return botConfiguration;
    }

    @Override
    public String getToken() {
        return botConfiguration.getToken();
    }

    @Override
    public SimpleTelegramLogger getLogger() {
        return logger;
    }

    private Connection connect(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private UserSession getOrCreateUserSession(UpdateWrapper updateWrapper) throws IOException {

        Optional<Chat> chatOpt = updateWrapper.getChat();
        if (chatOpt.isEmpty()) {

            throw new RuntimeException("Chat is null");
        }

        Optional<User> userOpt = updateWrapper.getUser();
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Chat is null");
        }

        long userId  = userOpt.get().id();
        long chatId = chatOpt.get().id();
        String username = userOpt.get().username();
        if (!updateWrapper.isMessage())
        {
            userId  = chatOpt.get().id();
            chatId = userOpt.get().id();
            username = chatOpt.get().username();
        }

        BotUser user = new CachedPgUsers(getSource()).findOrCreateUser(userId, username);

        UserSession userSession = userSessionMap.putIfAbsent(userId, new UserSession(this, user, chatId));
        if (userSession == null) {
            userSession = userSessionMap.get(userId);
            userSession.setName(username);
            userSession.start();
        }
        return userSession;
    }

    @Override
    public DataSource getSource() {
        return new PgSource(botConfiguration.getDatabaseUrl(), botConfiguration.getDatabaseUser(), botConfiguration.getDatabasePassword());
    }
}
