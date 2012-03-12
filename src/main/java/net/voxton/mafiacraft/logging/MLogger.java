/*
 * This file is part of Mafiacraft.
 * 
 * Mafiacraft is released under the Voxton License version 1.
 *
 * Mafiacraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition to this, you must also specify that this product includes 
 * software developed by Voxton.net and may not remove any code
 * referencing Voxton.net directly or indirectly.
 * 
 * Mafiacraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.logging;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.voxton.mafiacraft.Mafiacraft;

/**
 * A class to log.
 */
public class MLogger {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Mafiacraft.getImpl().getLogger();

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
