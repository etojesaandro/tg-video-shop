package ru.saandro.telegram.shop.persistence.entities;

import java.util.*;

public interface Users {
    Optional<? extends BotUser> findUser(long userId);

    Optional<? extends BotUser> findOrCreateUser(Long id, String name);
}
