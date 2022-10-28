package ru.saandro.telegram.shop.controller;

import java.util.Locale;
import java.util.Optional;

public enum ControlRoomCommands implements EnumWithDescription {
    UPLOAD("\uD83C\uDFA5 Загрузить Видео"),
    PROCESS_GENRE("\uD83C\uDFA5 Добавить/Удалить Жанр"),
    STATISTIC("\uD83D\uDCCA Статистика"),
    PROMOTE("\uD83D\uDC6E\u200D♀️ Назначить Администратора"),
    BACK("⬅️ Назад");

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
