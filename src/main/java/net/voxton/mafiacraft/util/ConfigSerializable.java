/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.util;

import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author simplyianm
 */
public interface ConfigSerializable {
    /**
     * Loads the object from config.
     *
     * @param source
     * @return
     */
    public ConfigSerializable load(ConfigurationSection source);

    /**
     * Saves the object to config.
     *
     * @param dest
     * @return
     */
    public ConfigSerializable save(ConfigurationSection dest);

}
