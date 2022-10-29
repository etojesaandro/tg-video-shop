package ru.saandro.telegram.shop.core;

import java.io.*;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;

public interface ScreenController {
    void processCallback(CallbackQuery callbackQuery) throws IOException;

    void onStart() throws IOException;

    void processMessage(Message message) throws IOException;
}
