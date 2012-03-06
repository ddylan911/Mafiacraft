/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.util;

import net.voxton.mafiacraft.MConfig;

/**
 * Contains various methods for validating strings for use within the plugin.
 */
public class ValidationUtils {

    /**
     * Validates a name to be easy to type in chat.
     *
     * @param name The name to validate.
     * @return The first error found with the name.
     */
    public static String validateName(String name) {
        int max = MConfig.getInt("division.maxnamelength");
        if (name.length() > max) {
            return "Name must be under " + max + " characters.";
        }

        if (!name.matches("[A-Za-z0-9]+")) {
            return "Name must be alphanumeric.";
        }

        return null;
    }

    /**
     * Validates a description.
     *
     * @param description The description to validate.
     * @return The first error found with the description,
     */
    public static String validateDescription(String description) {
        int max = MConfig.getInt("district.maxdesclength");
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
