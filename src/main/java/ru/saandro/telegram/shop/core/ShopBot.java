package ru.saandro.telegram.shop.core;

import javax.sql.DataSource;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

import liquibase.exception.*;
import ru.saandro.telegram.shop.conf.BotConfiguration;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;
import ru.saandro.telegram.shop.persistence.entities.*;

import java.sql.*;

public interface ShopBot {

    void start() throws LiquibaseException, SQLException;

    boolean isRunning();

    <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request);

    BotConfiguration getConfiguration();

    String getToken();

    SimpleTelegramLogger getLogger();

    DataSource getSource() throws SQLException;
}
