/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to log.
 */
public class MLogger {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Mafiacraft.getPlugin().getLogger();

    /**
     * The verbosity to use.
     */
    private static final int VERBOSITY = 5;

    /**
     * Logs a message with a level of info.
     *
     * @param message The message to log.
     */
    public static void log(String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message to the console.
     *
     * @param level The logging level to use.
     * @param msg The message to send.
     */
    public static void log(Level level, String msg) {
        LOGGER.log(level, msg);
    }

    /**
     * Logs an error to the console.
     *
     * @param level The logging level to use.
     * @param msg The message to send.
     * @param thrown The Throwable to log.
     */
    public static void log(Level level, String msg, Throwable thrown) {
        LOGGER.log(level, msg, thrown);
    }

    /**
     * Logs a message with a verbosity of 1.
     *
     * @param message The message to log.
     */
    public static void logVerbose(String message) {
        logVerbose(message, 1);
    }

    /**
     * Logs a message with the specified verbosity.
     *
     * @param message The message to log
     * @param level The verbosity to use.
     */
    public static void logVerbose(String message, int level) {
        if (level > VERBOSITY) {
            return;
        }
        log("[V" + level + "] " + message);
    }

}
