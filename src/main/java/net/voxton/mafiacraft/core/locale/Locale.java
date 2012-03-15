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
package net.voxton.mafiacraft.core.locale;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.voxton.mafiacraft.core.chat.MsgColor;

/**
 * Represents a locale.
 */
public class Locale {

    /**
     * Map which containes all localized strings of the locale.
     */
    private Map<String, String> localizedStrings = new HashMap<String, String>();

    /**
     * The name of the locale.
     */
    private final String name;

    /**
     * Protected constructor
     */
    Locale(String name) {
        this.name = name;
    }

    /**
     * Gets the localized version of a string.
     *
     * @param string The string to localize.
     * @return The localized string.
     */
    public String localize(String string) {
        String lcl = localizedStrings.get(string);
        if (lcl == null) {
            lcl = string;
        }
        return lcl;
    }

    /**
     * Localizes and formats a string.
     *
     * @param string The string to localize.
     * @param vars The variable to localize with.
     * @return The localized string.
     */
    public String localize(String string, Object... vars) {
        return String.format(localize(string), vars);
    }

    /**
     * Gets the name of the locale.
     *
     * @return The name of the locale.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a localization to the locale.
     *
     * @param string The string to localize.
     * @param localized The localized string.
     */
    void addLocalization(String string, String localized) {
        localizedStrings.put(string.toLowerCase(), MsgColor.parseColors(
                localized));
    }

    /**
     * Gets the entryset of the locale strings.
     *
     * @return The entryset.
     */
    Set<Entry<String, String>> getEntries() {
        return localizedStrings.entrySet();
    }

    @Override
    public String toString() {
        return "Locale{name=" + name + '}';
    }

}
