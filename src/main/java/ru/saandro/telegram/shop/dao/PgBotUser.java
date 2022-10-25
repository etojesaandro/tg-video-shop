package ru.saandro.telegram.shop.dao;

import javax.sql.DataSource;

import com.pengrad.telegrambot.model.User;

public class PgBotUser implements BotUser {

    private final long uid;
    private final String name;

    public PgBotUser(DataSource source, User user) {
        uid = user.id();
        name = user.username();
    }

    public long uid() {
        return uid;
    }

    public String name() {
        return name;
    }


    @Override
    public boolean isAdmin() {
        return true;
    }
}
