package ru.saandro.telegram.shop.core;

import java.util.Optional;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;

public class UpdateWrapper {

    public final Update update;

    public UpdateWrapper(Update update) {
        this.update = update;
    }

    public Optional<User> getUser() {
        if (update.message() != null) {
            return Optional.of(update.message().from());
        } else if (update.callbackQuery() != null) {
            return Optional.of(update.callbackQuery().message().from());
        } else {
            return Optional.empty();
        }
    }

    public boolean isMessage() {
        return update.message() != null;
    }

    public boolean isCallbackQuery() {
        return update.callbackQuery() != null;
    }

    public Optional<Chat> getChat() {
        if (update.message() != null) {
            return Optional.of(update.message().chat());
        } else if (update.callbackQuery() != null) {
            return Optional.of(update.callbackQuery().message().chat());
        } else {
            return Optional.empty();
        }
    }
}
