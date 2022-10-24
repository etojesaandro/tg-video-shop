package ru.saandro.telegram.shop.core;

import com.pengrad.telegrambot.model.CallbackQuery;

public interface ScreenController {
    void processCallback(CallbackQuery callbackQuery);

    void onStart();
}
