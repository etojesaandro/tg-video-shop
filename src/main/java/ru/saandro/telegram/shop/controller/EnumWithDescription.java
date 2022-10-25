package ru.saandro.telegram.shop.controller;

import java.util.EnumSet;
import java.util.Optional;

public interface EnumWithDescription {
    String getName();

    String getDescription();

    boolean isAdmin();

    static <E extends Enum<E> & EnumWithDescription> Optional<E> parse(String text, Class<E> enumClass) {
        EnumSet<E> es = EnumSet.allOf(enumClass);
        for (E value : es) {
            if (value.getName().equalsIgnoreCase(text)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
