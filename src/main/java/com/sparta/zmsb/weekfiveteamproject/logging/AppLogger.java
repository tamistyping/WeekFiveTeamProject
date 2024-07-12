package com.sparta.zmsb.weekfiveteamproject.logging;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {
    private static final Logger logger = Logger.getLogger(AppLogger.class.getName());

    public static void configure() {
        for (var handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        try {
            setupLog("src/main/resources/log.log", Level.ALL, logger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setupLog(String pattern, Level all, Logger logger) throws IOException {
        FileHandler logHandler = new FileHandler(pattern);
        logHandler.setLevel(all);
        logHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(logHandler);
    }

    public static Logger getLogger() {
        return logger;
    }
}