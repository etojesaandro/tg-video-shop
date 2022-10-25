package ru.saandro.telegram.shop.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import javax.sql.DataSource;

import com.jcabi.jdbc.StaticSource;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;

import ru.saandro.telegram.shop.conf.BotCommands;
import ru.saandro.telegram.shop.conf.BotConfiguration;
import ru.saandro.telegram.shop.conf.SetMyCommandsImpl;
import ru.saandro.telegram.shop.dao.PgBotUser;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;
import ru.saandro.telegram.shop.session.UserSession;

public class ShopBotImpl implements ShopBot {

    private final TelegramBot telegramBot;

    private final SimpleTelegramLogger logger;

    private BotConfiguration botConfiguration;

    private DataSource dataSource;

    private final ConcurrentHashMap<Long, UserSession> userSessionMap = new ConcurrentHashMap<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    public ShopBotImpl(BotConfiguration botConfiguration, SimpleTelegramLogger simpleTelegramLogger) {

        this.telegramBot = new TelegramBot(botConfiguration.getToken());
        this.botConfiguration = botConfiguration;
        this.logger = simpleTelegramLogger;
        Connection connect = connect(botConfiguration.getDatabaseUrl(), botConfiguration.getDatabaseUser(), botConfiguration.getDatabasePassword());
        dataSource = new StaticSource(connect);
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

    @Override
    public DataSource getDataSource() {
        return dataSource;
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

    private Connection connect(String url, String user, String password) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            getLogger().log(Level.WARNING, "Preview loading error", e);
        }

        return conn;
    }

    private UserSession getOrCreateUserSession(UpdateWrapper updateWrapper) {

        Optional<Chat> chatOpt = updateWrapper.getChat();
        if (chatOpt.isEmpty()) {
            throw new RuntimeException("Chat is null");
        }

        Optional<User> userOpt = updateWrapper.getUser();
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Chat is null");
        }

        Chat chat = chatOpt.get();
        User user = userOpt.get();
        UserSession userSession = userSessionMap.putIfAbsent(chat.id(), new UserSession(this, new PgBotUser(dataSource, user), chat));
        if (userSession == null) {
            userSession = userSessionMap.get(chat.id());
            userSession.start();
        }
        return userSession;
    }
}
