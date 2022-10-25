package ru.saandro.telegram.shop.core;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;

public interface ScreenController {
    void processCallback(CallbackQuery callbackQuery);

    void onStart();

    void processMessage(Message message);
}
