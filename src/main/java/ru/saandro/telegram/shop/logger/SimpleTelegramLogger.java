package ru.saandro.telegram.shop.logger;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SimpleTelegramLogger {

    public void log(Level level, String msg) {
        LogRecord lr = new LogRecord(level, msg);
        doLog(lr);
    }


    public void log(Level level, String msg, Throwable thrown) {
        LogRecord lr = new LogRecord(level, msg);
        lr.setThrown(thrown);
        doLog(lr);
    }

    public void log(Level level, String msg, Object[] params) {
        LogRecord lr = new LogRecord(level, msg);
        lr.setParameters(params);
        doLog(lr);
    }

    private void doLog(LogRecord lr) {

    }
}
