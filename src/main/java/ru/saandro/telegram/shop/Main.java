package ru.saandro.telegram.shop;

import liquibase.exception.*;
import ru.saandro.telegram.shop.conf.DefaultBotConfiguration;
import ru.saandro.telegram.shop.core.ShopBotImpl;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws LiquibaseException, SQLException {
        new ShopBotImpl(
                new DefaultBotConfiguration(),
                new SimpleTelegramLogger()
        ).start();
    }
}
