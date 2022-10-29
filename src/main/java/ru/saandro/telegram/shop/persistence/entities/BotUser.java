package ru.saandro.telegram.shop.persistence.entities;

import java.io.*;
import java.sql.SQLException;

public interface BotUser {

    Long id();

    String name() throws IOException;

    Integer balance() throws IOException;

    boolean admin() throws IOException;

    void purchaseItem(Item itemToPurchase) throws IOException;

    BotUser updateBalance(Integer balance) throws IOException;

    BotUser promote() throws IOException;

    boolean hasItem(Item item) throws IOException;
}

