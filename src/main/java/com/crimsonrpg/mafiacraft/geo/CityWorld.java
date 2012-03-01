/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import com.crimsonrpg.mafiacraft.util.ConfigSerializable;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents a world that contains cities.
 */
public class CityWorld implements ConfigSerializable {
    private final World world;

    private City capital;

    /**
     * True if players in this world are allowed to walk outside of the city and
     * build etc. there.
     */
    private boolean freeRoam = true;

    public CityWorld(World world) {
        this.world = world;
    }

    /**
     * Gets the World this CityWorld is associated with.
     *
     * @return
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the capital of the city world.
     *
     * @return
     */
    public City getCapital() {
        return capital;
    }

    /**
     * Sets the capital of the city world.
     *
     * @param capital
     * @return
     */
    public CityWorld setCapital(City capital) {
        this.capital = capital;
        return this;
    }

    /**
     * Returns true if this world allows free roaming of the world.
     *
     * @return
     */
    public boolean isFreeRoam() {
        return freeRoam;
    }

    public ConfigSerializable load(ConfigurationSection source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ConfigSerializable save(ConfigurationSection dest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
