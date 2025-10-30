package ru.saandro.telegram.shop.logger;

import ru.saandro.telegram.shop.conf.Log;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SimpleTelegramLogger {

    public void log(Level level, String msg) {
        Log.LOGGER.log(level, msg);
    }

    public void log(Level level, String msg, Throwable thrown) {
        Log.LOGGER.log(level, msg, thrown);
    }

    public void log(Level level, String msg, Object[] params) {
        Log.LOGGER.log(level, msg, params);
    }
}
