/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import com.crimsonrpg.mafiacraft.util.ConfigSerializable;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents a world that contains cities.
 */
public class CityWorld implements ConfigSerializable {
    private final World world;

    private City capital;

    /**
     * Holds all toggles. True if the set contains the toggle.
     */
    private Set<String> toggles = new HashSet<String>();

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
        capital.setCityWorld(this);
        return this;
    }

    /**
     * Removes the capital.
     *
     * @return
     */
    public CityWorld removeCapital() {
        capital.setCityWorld(null);
        capital = null;
        return this;
    }

    /**
     * Returns true if the given WorldToggle is toggled.
     */
    public boolean isToggled(WorldToggle toggle) {
        return toggles.contains(toggle.name());
    }

    /**
     * Sets a value as toggled or not toggled.
     *
     * @param toggle
     * @param value
     * @return True if the value of the toggle changed.
     */
    public boolean setToggled(WorldToggle toggle, boolean value) {
        boolean toggled = isToggled(toggle);
        if (value && !toggled) {
            toggles.add(toggle.name());
            return true;
        } else if (!value && toggled) {
            return true;
        }
        return false;
    }

    /**
     * Toggles a toggle.
     *
     * @param toggle
     * @return The new value of the toggle.
     */
    public boolean toggle(WorldToggle toggle) {
        boolean value = !isToggled(toggle);
        setToggled(toggle, value);
        return value;
    }

    public DistrictType getDefaultDistrictType() {
        boolean freeRoam = isToggled(WorldToggle.FREE_ROAM);
        return freeRoam ? DistrictType.ANARCHIC : DistrictType.RESERVED;
    }

    public ConfigSerializable load(ConfigurationSection source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ConfigSerializable save(ConfigurationSection dest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
