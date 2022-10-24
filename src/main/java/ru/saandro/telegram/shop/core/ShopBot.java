package ru.saandro.telegram.shop.core;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

public interface ShopBot {

    void start();

    boolean isRunning();

    <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request);
}
