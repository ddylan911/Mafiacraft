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
package net.voxton.mafiacraft.core.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Contains utilities for manipulation strings.
 */
public class StringUtils {

    /**
     * Formats a number to the currency used on the server.
     *
     * @param num The number to format.
     * @return The formatted number.
     */
    public static String formatCurrency(double num) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(num);
    }

    /**
     * Formats a string to capitalize the first letter of each word.
     *
     * @param string The string to format.
     * @return The formatted string.
     */
    public static String titleize(String string) {
        if (string.length() < 1) {
            return string;
        }

        StringBuilder builder = new StringBuilder(string.toLowerCase());

        //Capitalize first
        builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));

        int index;
        for (int i = 0; (index = builder.indexOf(" ", i)) > 0; i++) {
            index++;
            if (builder.length() <= index) {
                continue;
            }
            char after = builder.charAt(index);
            after = Character.toUpperCase(after);
            builder.setCharAt(index, after);
        }
        return builder.toString();
    }

}
