package ru.saandro.telegram.shop.persistence.entities;

import java.io.*;
import java.util.*;

public interface Users {
    Optional<? extends BotUser> findUser(long userId) throws IOException;

    Optional<? extends BotUser> findUser(String name) throws IOException;

    BotUser findOrCreateUser(Long id, String name) throws IOException;

    BotUser add(Long id, String name, Integer balance, boolean admin) throws IOException;
}
