package com.github.indigopolecat.bingobrewers.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Log {
    public static final Logger LOG = LoggerFactory.getLogger("bingobrewers");
    // huds render on the client thread but are built on the network thread, so this has to be thread safe
    private static final Set<String> warnedOnce = ConcurrentHashMap.newKeySet();

    /**
     * Logs a warning the first time it is called with a given key, then stays quiet for that key.
     * For warnings raised from code that runs every frame, where the plain {@link #warn(String)} would spam the log.
     *
     * @param key what to deduplicate on. Include the offending value to get a fresh warning when it changes
     * @param message the message to log on the first call
     */
    public static void warnOnce(String key, String message) {
        if(warnedOnce.add(key)) LOG.warn(message);
    }

    public static void info(String message) {
        LOG.info(message);
    }
    
    public static void info(String message, Throwable throwable) {
        LOG.info(message, throwable);
    }
    
    public static void warn(String message) {
        LOG.warn(message);
    }
    
    public static void warn(String message, Throwable throwable) {
        LOG.warn(message, throwable);
    }
    
    public static void error(String message) {
        LOG.error(message);
    }
    
    public static void error(String message, Throwable throwable) {
        LOG.error(message, throwable);
    }
}
