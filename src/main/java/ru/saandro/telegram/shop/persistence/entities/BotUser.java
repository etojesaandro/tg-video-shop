package ru.saandro.telegram.shop.persistence.entities;

import java.util.List;

public interface BotUser {

    List<PgItem> getPurchasedItems();

    boolean isAdmin();

    String name();

    long id();

    long balance();

    void purchaseItem(Item itemToPurchase);
}

