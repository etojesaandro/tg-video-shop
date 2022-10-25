package ru.saandro.telegram.shop;

import ru.saandro.telegram.shop.conf.DebugBotConfiguration;
import ru.saandro.telegram.shop.core.ShopBotImpl;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;

public class Main {
    public static void main(String[] args) {
        new ShopBotImpl(
                new DebugBotConfiguration(),
                new SimpleTelegramLogger()
        ).start();
    }
}
