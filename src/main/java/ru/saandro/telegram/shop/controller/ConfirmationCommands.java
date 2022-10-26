package ru.saandro.telegram.shop.controller;

import java.util.Locale;

public enum ConfirmationCommands implements EnumWithDescription {
    YES("Да"),
    NO("Нет"),
    BACK("⬅️ Назад");

    public final String name;
    public final String descr;

    ConfirmationCommands(String descr) {
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
