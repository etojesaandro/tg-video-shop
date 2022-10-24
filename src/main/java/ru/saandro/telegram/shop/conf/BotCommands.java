package ru.saandro.telegram.shop.conf;

import java.util.Locale;

public enum BotCommands {
    START("Restart Bot");

    public final String name;
    public final String descr;

    BotCommands(String descr) {
        this.name = "/" + name().toLowerCase(Locale.ROOT);
        this.descr = descr;
    }

    public static BotCommands parse(String text) {
        for (BotCommands value : BotCommands.values()) {
            if (value.name.equals(text)) {
                return value;
            }
        }
        return null;
    }
}
