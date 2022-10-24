package ru.saandro.telegram.shop.controller;

import java.util.Locale;
import java.util.Optional;

public enum HomeScreenCommands implements EnumWithDescription {
    BUY_VIDEOS("Buy Videos"),
    MY_VIDEOS("My Videos"),
    DONATE("Donate");

    public final String name;
    public final String descr;

    HomeScreenCommands(String descr) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.descr = descr;
    }

    public static Optional<HomeScreenCommands> parse(String text) {
        for (HomeScreenCommands value : HomeScreenCommands.values()) {
            if (value.name.equals(text)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return descr;
    }
}
