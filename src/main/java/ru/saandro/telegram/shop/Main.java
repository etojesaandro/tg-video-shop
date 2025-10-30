package ru.saandro.telegram.shop;

import liquibase.exception.*;
import ru.saandro.telegram.shop.conf.DefaultBotConfiguration;
import ru.saandro.telegram.shop.conf.Log;
import ru.saandro.telegram.shop.core.ShopBotImpl;
import ru.saandro.telegram.shop.logger.SimpleTelegramLogger;

import java.net.URL;
import java.sql.*;

import com.google.common.io.Resources;

public class Main {
    public static void main(String[] args) throws LiquibaseException, SQLException {

        startLogging();

        new ShopBotImpl(
                new DefaultBotConfiguration(),
                new SimpleTelegramLogger()
        ).start();
    }

    public static void startLogging()
    {
        try
        {
            URL defaultLoggingProperties = Resources.getResource(Log.class, "logging.properties");
            Log.init(defaultLoggingProperties);
        }
        catch (Throwable ex)
        {
            System.out.println("Ошибка конфигурации логгера");
            ex.printStackTrace();
        }

    }
}