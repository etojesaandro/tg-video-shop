package ru.saandro.telegram.shop.controller;

import java.util.Locale;
import java.util.Optional;

public enum ControlRoomCommands implements EnumWithDescription {
    UPLOAD("Загрузить Видео"),
    STATISTIC("Статистика"),
    PROMOTE("Назначить Администратора"),
    BACK("Назад");

    public final String name;
    public final String descr;

    ControlRoomCommands(String descr) {
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
        return true;
    }
}
