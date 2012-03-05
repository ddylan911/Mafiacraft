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
    private static final Logger LOGGER = Mafiacraft.getPlugin().getLogger();

    private static final int VERBOSITY = 5;

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static void log(Level level, String msg) {
        msg = "[MC] " + msg;
        LOGGER.log(level, msg);
    }

    public static void log(Level level, String msg, Throwable thrown) {
        msg = "[MC] " + msg;
        LOGGER.log(level, msg, thrown);
    }

    public static void logVerbose(String message) {
        logVerbose(message, 1);
    }

    public static void logVerbose(String message, int level) {
        if (level > VERBOSITY) {
            return;
        }
        log("[V" + level + "] " + message);
    }

}
