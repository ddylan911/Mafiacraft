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
package net.voxton.mafiacraft.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;

/**
 * Holds message colors.
 * 
 * <p>Note: I include Bukkit references here because I think they are set
 * at compile time. If they aren't... well... crap.</p>
 */
public class MsgColor {

    public static final String ERROR = ChatColor.RED.toString();
    
    public static final String ERROR_HILIGHT = ChatColor.YELLOW.toString();

    public static final String INFO = ChatColor.YELLOW.toString();

    public static final String INFO_HILIGHT = ChatColor.GOLD.toString();

    public static final String INFO_GOV = ChatColor.GREEN.toString();

    public static final String SUCCESS = ChatColor.GREEN.toString();
    
    public static final String SUCCESS_HILIGHT = ChatColor.YELLOW.toString();

    public static final String URL = ChatColor.AQUA.toString();

    public static final String CHAT_ADMIN = ChatColor.GOLD.toString();

    public static final String CHAT_CITY = ChatColor.DARK_GREEN.toString();

    public static final String CHAT_DISTRICT = ChatColor.BLUE.toString();

    public static final String CHAT_GLOBAL = ChatColor.GREEN.toString();

    public static final String CHAT_LOCAL = ChatColor.LIGHT_PURPLE.toString();

    public static final String HELP_ENTRY = ChatColor.GREEN.toString();

    public static final String HELP_DEF = ChatColor.YELLOW.toString();
    
    public static final String NORMAL = ChatColor.WHITE.toString();
    
    public static final String MAFIA = ChatColor.DARK_RED.toString();
    
    public static final String REGIME = ChatColor.GOLD.toString();

    public static final String OFFICER = ChatColor.GREEN.toString();
    
    public static final String POLICE = ChatColor.BLUE.toString();
    
    public static final String SQUAD = ChatColor.AQUA.toString();
    
    public static final String COMMANDER = ChatColor.GRAY.toString();

    private static final Map<String, String> colors = new HashMap<String, String>();
    
    /**
     * Parses colors.
     * 
     * @param uncolored The uncolored string full of color variables.
     * @return The string, brand new and colored.
     */
    public static String parseColors(String uncolored) {
        for (Entry<String, String> colorEntry : colors.entrySet()) {
            uncolored = uncolored.replace("`{" + colorEntry.getKey() + "}", colorEntry.getValue());
        }
        return uncolored;
    }
    
    /**
     * Gets a color from its name.
     * 
     * @param colorName The name of the color.
     * @return The color in string form.
     */
    public static String getColor(String colorName) {
        return colors.get(colorName.toUpperCase());
    }
    
    public static String stripColor(String string) {
        return ChatColor.stripColor(string);
    }
    
    static {
        colors.put("ERROR", ERROR);
        colors.put("ERROR_HILIGHT", ERROR_HILIGHT);
        colors.put("INFO", INFO);
        colors.put("NORMAL", NORMAL);
        colors.put("URL", URL);
        colors.put("SUCCESS", SUCCESS);
        colors.put("SUCCESS_HILIGHT", SUCCESS_HILIGHT);
    }
}
