package ru.saandro.telegram.shop.persistence.entities;

import ru.saandro.telegram.shop.core.*;

import javax.sql.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.google.common.collect.*;
import com.jcabi.jdbc.*;


public class PgUser implements BotUser {

    private final ShopBot provider;
    private final long id;
    private final String name;
    private final long balance;

    public PgUser(ShopBot provider, com.pengrad.telegrambot.model.User user) {
        this.provider = provider;
        id = user.id();
        name = user.username();
        this.balance = 0;
    }

    public PgUser(ShopBot provider, long uid, String name, int balance) {
        this.provider = provider;
        this.id = uid;
        this.name = name;
        this.balance = balance;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public long balance() {
        return balance;
    }

    @Override
    public List<PgItem> getPurchasedItems() {
        return ImmutableList.of();
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    public Optional<? extends BotUser> create() {
        try {
            DataSource source = provider.getSource();
            new JdbcSession(source)
                    .sql("INSERT INTO BOT_USER(ID, NAME ,BALANCE) VALUES(?,?,?)")
                    .set(id).set(name).set(0).execute();
        } catch (SQLException e) {
            provider.getLogger().log(Level.WARNING, "Unable to create new user", e);
            return Optional.empty();
        }
        return Optional.of(this);
    }
}
