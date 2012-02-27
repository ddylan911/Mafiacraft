/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

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
    private static final Map<String, Object> defaults = new HashMap<String, Object>();

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
                String key = def.getKey();
                if (!conf.isSet(key)) {
                    conf.set(key, def.getValue());
                }
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
        //Currency
        addDefault("currency.name", "dollar");
        addDefault("currency.namepl", "dollars");


        //Mafia prices
        addDefault("prices.mafia.found", 150000.0);

        //City prices
        addDefault("prices.city.annex", 10000.0);
        addDefault("prices.city.claim", 100.0);
        addDefault("prices.city.found", 1000000.0);
        
        //Government
        addDefault("prices.gov.sethq", 100.0);

        //Division
        addDefault("division.maxnamelength", 15);

        //District
        addDefault("district.maxdesclength", 45);
        
        //Spawn times
        addDefault("warmup.cityspawn", 10);
    }

}
