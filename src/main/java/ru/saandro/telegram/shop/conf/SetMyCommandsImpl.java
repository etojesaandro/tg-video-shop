package ru.saandro.telegram.shop.conf;

import com.pengrad.telegrambot.request.SetMyCommands;

public class SetMyCommandsImpl extends SetMyCommands {
    public SetMyCommandsImpl(BotCommands[] values) {
        super();
        for (BotCommands value : values) {
            add(value.name, value.descr);
        }
    }
}
