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
package net.voxton.mafiacraft.impl.bukkit;

import net.voxton.mafiacraft.config.MafiacraftConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.Configuration;

/** 
 * Mafiacraft configuration.
 */
public class BukkitConfig implements MafiacraftConfig {

    private final Map<String, Object> defaults =
            new HashMap<String, Object>();

    private final BukkitImpl impl;

    private final Configuration config;

    public BukkitConfig(BukkitImpl impl) {
        this.impl = impl;
        this.config = impl.getConfig();

        for (Entry<String, Object> def : defaults.entrySet()) {
            config.addDefault(def.getKey(), def.getValue());
        }
    }

    @Override
    public List<String> getStringList(String string) {
        return config.getStringList(string);
    }

    @Override
    public String getString(String string) {
        return config.getString(string);
    }

    @Override
    public long getLong(String string) {
        return config.getLong(string);
    }

    @Override
    public int getInt(String string) {
        return config.getInt(string);
    }

    @Override
    public double getDouble(String string) {
        return config.getDouble(string);
    }

    @Override
    public List<Boolean> getBooleanList(String string) {
        return config.getBooleanList(string);
    }

    @Override
    public boolean getBoolean(String string) {
        return config.getBoolean(string);
    }

    @Override
    public Object get(String string) {
        return config.get(string);
    }

    /**
     * Gets the configuration.
     *
     * @return
     */
    private Configuration getConfig() {
        return config;
    }

    /**
     * Sets a certain path to a certain value. (Persists)
     *
     * @param path
     * @param value
     */
    @Override
    public void set(String path, Object value) {
        config.set(path, value);
        saveConfig();
    }

    /**
     * Saves the configuration.
     */
    @Override
    public void saveConfig() {
        impl.saveConfig();
    }

    /**
     * Adds a default config setting.
     *
     * @param key
     * @param value
     */
    private void addDefault(String key, Object value) {
        defaults.put(key, value);
    }

    {
        //Locale
        addDefault("locale.default", "en-us");

        //Currency
        addDefault("currency.name", "dollar");
        addDefault("currency.namepl", "dollars");

        //City prices
        addDefault("prices.city.annex", 10000.0);
        addDefault("prices.city.claim", 100.0);

        //Government
        addDefault("prices.gov.sethq", 100.0);

        //Mafia
        addDefault("mafia.found", 150000.0);
        addDefault("mafia.startupcapital", 100000.0);
        addDefault("mafia.regimestartup", 5000.0);

        //String standardization
        addDefault("strings.maxnamelength", 15);
        addDefault("strings.maxdesclength", 45);

        //District
        addDefault("district.bus-max-distance", 10);

        //City
        addDefault("city.foundcost", 10000000.0);

        //Spawn times
        addDefault("warmup.cityspawn", 10);
    }

}
