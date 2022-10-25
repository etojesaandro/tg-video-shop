package ru.saandro.telegram.shop.controller;

import java.util.Locale;
import java.util.Optional;

public enum HomeScreenCommands implements EnumWithDescription {
    BUY_VIDEOS("Buy Videos"),
    MY_VIDEOS("My Videos"),
    DONATE("Donate"),
    CONTROL_ROOM("Control Room", true);

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
