package ru.saandro.telegram.shop.controller;

import java.util.Locale;
import java.util.Optional;

public enum VideoGenres implements EnumWithDescription {
    ALL("Все"),
    FOOT("Жесть"),
    SCARFING("Полная жесть");

    public final String name;
    public final String descr;

    VideoGenres(String descr) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.descr = descr;
    }

    public static Optional<VideoGenres> parse(String text) {
        for (VideoGenres value : VideoGenres.values()) {
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
