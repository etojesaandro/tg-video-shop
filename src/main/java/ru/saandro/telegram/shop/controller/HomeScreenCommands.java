package ru.saandro.telegram.shop.controller;

import java.util.Locale;
import java.util.Optional;

public enum HomeScreenCommands implements EnumWithDescription {
    BUY_VIDEOS("\uD83C\uDF46 Видео по Жанрам"),
    MY_VIDEOS("❤️ Мои видео"),
    DONATE("💵 Задонатить Нам"),
    CONTROL_ROOM("\uD83D\uDCBB Администрирование", true);

    public final String name;
    public final String descr;
    public final boolean admin;

    HomeScreenCommands(String descr) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.descr = descr;
        this.admin = false;
    }

    HomeScreenCommands(String descr, boolean admin) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.descr = descr;
        this.admin = admin;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return descr;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }
}
