/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.geo;

import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * Represents a world that contains cities.
 */
@SerializableAs("cw")
public class CityWorld implements ConfigurationSerializable {

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

    /**
     * Gets the default district type of this CityWorld.
     *
     * @return The default district type.
     */
    public DistrictType getDefaultDistrictType() {
        boolean freeRoam = isToggled(WorldToggle.FREE_ROAM);
        return freeRoam ? DistrictType.ANARCHIC : DistrictType.RESERVED;
    }

    /**
     * Gets the name of this CityWorld.
     *
     * @return The name of the CityWorld.
     */
    public String getName() {
        return getWorld().getName();
    }

    ////////////
    // SERIALIZATION
    ////////////
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("toggles", new ArrayList<String>(toggles));
        data.put("world", getName());
        return data;
    }

    /**
     * Deserializes a serialized CityWorld.
     *
     * @param data The data in Map form.
     * @return The deserialized CityWorld object.
     */
    public static CityWorld deserialize(Map<String, Object> data) {
        String worldName = data.get("world").toString();
        World world = Bukkit.getWorld(worldName);
        CityWorld cw = new CityWorld(world);

        List<String> toggles = (List<String>) data.get("toggles");
        for (String toggle : toggles) {
            WorldToggle tog = null;
            try {
                tog = WorldToggle.valueOf(toggle);
                cw.setToggled(tog, true);
            } catch (IllegalArgumentException ex) {
            }
        }

        return cw;
    }

    /**
     * Gets the spawn location of the city world.
     * 
     * @return The spawn location of the city world.
     */
    public Location getSpawnLocation() {
        City capital = getCapital();
        if (capital == null) {
            return null;
        }
        
        return capital.getSpawnLocation();
    }

}
