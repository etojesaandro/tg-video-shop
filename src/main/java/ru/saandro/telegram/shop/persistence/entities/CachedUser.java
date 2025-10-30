package ru.saandro.telegram.shop.persistence.entities;

import java.io.IOException;
import java.sql.SQLException;

public class CachedUser implements BotUser {

    private final BotUser origin;
    private final String name;
    private final Integer balance;
    private final boolean admin;

    public CachedUser(BotUser origin, String name, Integer balance, boolean admin) {
        this.origin = origin;
        this.name = name;
        this.balance = balance;
        this.admin = admin;
    }

    @Override
    public Long id() {
        return origin.id();
    }

    @Override
    public String name() throws IOException {
        return name;
    }

    @Override
    public Integer balance() throws IOException {
        return balance;
    }

    @Override
    public boolean admin() {
        return admin;
    }

    @Override
    public void purchaseItem(Item itemToPurchase) throws IOException {
        updateBalance(balance() - itemToPurchase.price());
        origin.purchaseItem(itemToPurchase);
    }

    @Override
    public BotUser updateBalance(Integer balance) throws IOException {
        origin.updateBalance(balance);
        return new CachedUser(this.origin, this.name, balance, this.admin);
    }

    @Override
    public BotUser promote() throws IOException {
        origin.promote();
        return new CachedUser(this.origin, this.name, balance, true);
    }

    @Override
    public boolean hasItem(Item item) throws IOException {
        return origin.hasItem(item);
    }
}
