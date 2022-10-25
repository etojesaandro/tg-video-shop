package ru.saandro.telegram.shop.core;

import javax.sql.DataSource;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

import ru.saandro.telegram.shop.conf.BotConfiguration;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;

public interface ShopBot {

    void start();

    boolean isRunning();

    <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request);

    DataSource getDataSource();

    BotConfiguration getConfiguration();

    String getToken();

    SimpleTelegramLogger getLogger();
}
