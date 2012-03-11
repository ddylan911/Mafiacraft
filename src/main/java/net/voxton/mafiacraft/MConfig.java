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
package net.voxton.mafiacraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.Configuration;

/**
 *
 * @author ianschool
 */
public class MConfig {

    private static final Map<String, Object> defaults =
            new HashMap<String, Object>();

    private static MafiacraftPlugin plugin = null;

    private static Configuration config = null;

    /**
     * Binds this singleton to the specified plugin.
     *
     * @param config
     */
    public static void bind(MafiacraftPlugin mc) {
        if (MConfig.plugin == null || MConfig.config == null) {
            Configuration conf = mc.getConfig();
            for (Entry<String, Object> def : defaults.entrySet()) {
                conf.addDefault(def.getKey(), def.getValue());
            }
            MConfig.plugin = mc;
            MConfig.config = conf;
        }
    }

    public static List<String> getStringList(String string) {
        return config.getStringList(string);
    }

    public static String getString(String string) {
        return config.getString(string);
    }

    public static long getLong(String string) {
        return config.getLong(string);
    }

    public static int getInt(String string) {
        return config.getInt(string);
    }

    public static double getDouble(String string) {
        return config.getDouble(string);
    }

    public static List<Boolean> getBooleanList(String string) {
        return config.getBooleanList(string);
    }

    public static boolean getBoolean(String string) {
        return config.getBoolean(string);
    }

    public static Object get(String string) {
        return config.get(string);
    }

    /**
     * Gets the configuration.
     *
     * @return
     */
    public static Configuration getConfig() {
        return config;
    }

    /**
     * Sets a certain path to a certain value. (Persists)
     *
     * @param path
     * @param value
     */
    public static void set(String path, Object value) {
        config.set(path, value);
        saveConfig();
    }

    /**
     * Saves the configuration.
     */
    public static void saveConfig() {
        plugin.saveConfig();
    }

    /**
     * Adds a default config setting.
     *
     * @param key
     * @param value
     */
    private static void addDefault(String key, Object value) {
        defaults.put(key, value);
    }

    static {
        //Locale
        addDefault("locale.default", "en-us");
        
        //Currency
        addDefault("currency.name", "dollar");
        addDefault("currency.namepl", "dollars");

        //City prices
        addDefault("prices.city.annex", 10000.0);
        addDefault("prices.city.claim", 100.0);
        addDefault("prices.city.found", 1000000.0);

        //Government
        addDefault("prices.gov.sethq", 100.0);

        //Mafia
        addDefault("mafia.found", 150000.0);
        addDefault("mafia.startupcapital", 100000.0);
        addDefault("mafia.regimestartup", 5000.0);

        //Division
        addDefault("division.maxnamelength", 15);

        //District
        addDefault("district.maxdesclength", 45);

        //Spawn times
        addDefault("warmup.cityspawn", 10);
    }

}
