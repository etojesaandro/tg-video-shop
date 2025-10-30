package ru.saandro.telegram.shop.conf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.WARNING;


public class Log {

    public static final Logger LOGGER = Logger.getLogger("bot");

    public static final Path LOGGING_ROOT_PATH = Paths.get(System.getProperty("user.dir"), "log");
    public static final Path LOGGING_PATH = LOGGING_ROOT_PATH.resolve("bot");

    private static final CharSequence JOINER = "\n ";
    private static final Path LOGGING_CONFIG = Paths.get("logging.properties");


    public static void init(URL defaultLoggingProperties) {
        requireNonNull(defaultLoggingProperties);
        try {
            Files.createDirectories(LOGGING_PATH);
            if (!LOGGING_CONFIG.toFile().exists()) {
                writeLoggingConfiguration(defaultLoggingProperties);
            }
            try (InputStream inputStream = Files.newInputStream(LOGGING_CONFIG)) {
                LogManager.getLogManager().readConfiguration(inputStream);
            }
        } catch (IOException e) {
            LOGGER.log(WARNING, "Unable to read configuration file", e);
        }
    }


    public static void writeLoggingConfiguration(URL defaultLoggingProperties) {
        try (InputStream inputStream = defaultLoggingProperties.openStream()) {
            Files.copy(inputStream, LOGGING_CONFIG);
        } catch (IOException e) {
            LOGGER.log(WARNING, "Error copying default logging properties", e);
        }
    }
}
