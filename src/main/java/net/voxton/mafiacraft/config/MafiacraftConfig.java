/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.config;

import java.util.List;
import org.bukkit.configuration.Configuration;

/**
 * Generic Mafiacraft Configuration Accessor.
 */
public interface MafiacraftConfig {

    public Object get(String string);

    public boolean getBoolean(String string);

    public List<Boolean> getBooleanList(String string);

    public double getDouble(String string);

    public int getInt(String string);

    public long getLong(String string);

    public String getString(String string);

    public List<String> getStringList(String string);

    /**
     * Saves the configuration.
     */
    public void saveConfig();

    /**
     * Sets a certain path to a certain value. (Persists)
     *
     * @param path
     * @param value
     */
    public void set(String path, Object value);

}
