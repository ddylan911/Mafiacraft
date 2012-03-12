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
package net.voxton.mafiacraft.util;

import net.voxton.mafiacraft.config.Config;

/**
 * Contains various methods for validating strings for use within the plugin.
 */
public class ValidationUtils {

    /**
     * Validates a name to be easy to type in chat.
     *
     * @param name The name to validate.
     * @return True if the name is valid.
     */
    public static boolean validateName(String name) {
        int max = Config.getInt("strings.maxnamelength");
        return name.length() <= max && name.matches("[A-Za-z0-9]+");
    }

    /**
     * Validates a description.
     *
     * @param description The description to validate.
     * @return The first error found with the description,
     */
    public static String validateDescription(String description) {
        int max = Config.getInt("strings.maxdesclength");
        if (description.length() > max) {
            return "Description must be under " + max + " characters.";
        }

        if (!description.matches("[\\w\\s\\d\\.]+")) {
            return "Names must consist of letters, numbers, underscores, and "
                    + "spaces.";
        }

        return null;
    }

}
