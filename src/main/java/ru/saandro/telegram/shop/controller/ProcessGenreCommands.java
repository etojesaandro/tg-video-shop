package ru.saandro.telegram.shop.controller;

import java.util.*;

public enum ProcessGenreCommands implements EnumWithDescription {
    ADD("Добавить Жанр"),
    DELETE("Удалить Жанр"),
    BACK("⬅️ Назад");

    public final String name;
    public final String descr;

    ProcessGenreCommands(String descr) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.descr = descr;
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
        return false;
    }
}
