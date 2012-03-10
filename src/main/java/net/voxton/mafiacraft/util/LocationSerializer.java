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
package net.voxton.mafiacraft.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.voxton.mafiacraft.MLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Serializes Locations into a Configuration-friendly form.
 */
public class LocationSerializer {

    /**
     * Serializes the integer Block aspects of a Location.
     *
     * @param loc The Location to serialize.
     * @return The serialized Location in Map form.
     */
    public static Map<String, Object> serializeBlock(Location loc) {
        if (loc == null) {
            return null;
        }

        Map<String, Object> data = new HashMap<String, Object>();

        data.put("world", loc.getWorld().getName());
        data.put("x", loc.getBlockX());
        data.put("y", loc.getBlockY());
        data.put("z", loc.getBlockZ());

        return data;
    }

    /**
     * Deserializes a block location.
     *
     * @param data The data to deserialize.
     * @return The deserialized block location.
     */
    public static Location deserializeBlock(Map<String, Object> data) {
        String worldS = data.get("world").toString();
        String xS = data.get("x").toString();
        String yS = data.get("y").toString();
        String zS = data.get("z").toString();

        World world = Bukkit.getWorld(worldS);
        if (world == null) {
            MLogger.log(Level.SEVERE, "Unknown world '" + worldS + "'!");
        }

        int x = 0;
        try {
            x = Integer.parseInt(xS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid x value to deserialize: '" + xS
                    + "'!", ex);
        }

        int y = 0;
        try {
            y = Integer.parseInt(yS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid y value to deserialize: '" + yS
                    + "'!", ex);
        }

        int z = 0;
        try {
            z = Integer.parseInt(zS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid z value to deserialize: '" + zS
                    + "'!", ex);
        }

        Location loc = new Location(world, x, y, z);
        return loc;
    }

    /**
     * Serializes all of the aspects of the Location.
     *
     * @param loc The Location to serialize.
     * @return The serialized Location in Map form.
     */
    public static Map<String, Object> serializeFull(Location loc) {
        if (loc == null) {
            return null;
        }

        Map<String, Object> data = new HashMap<String, Object>();

        data.put("world", loc.getWorld().getName());
        data.put("x", loc.getX());
        data.put("y", loc.getY());
        data.put("z", loc.getZ());
        data.put("yaw", loc.getYaw());
        data.put("pitch", loc.getPitch());

        return data;
    }

    /**
     * Deserializes a full location.
     *
     * @param data The data to deserialize.
     * @return The deserialized full location.
     */
    public static Location deserializeFull(Map<String, Object> data) {
        String worldS = data.get("world").toString();
        String xS = data.get("x").toString();
        String yS = data.get("y").toString();
        String zS = data.get("z").toString();
        String yawS = data.get("yaw").toString();
        String pitchS = data.get("pitch").toString();

        World world = Bukkit.getWorld(worldS);
        if (world == null) {
            MLogger.log(Level.SEVERE, "Unknown world '" + worldS + "'!");
        }

        double x = 0;
        try {
            x = Double.parseDouble(xS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid x value to deserialize: '" + xS
                    + "'!", ex);
        }

        double y = 0;
        try {
            y = Double.parseDouble(yS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid y value to deserialize: '" + yS
                    + "'!", ex);
        }

        double z = 0;
        try {
            z = Double.parseDouble(zS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid z value to deserialize: '" + zS
                    + "'!", ex);
        }

        float yaw = 0;
        try {
            yaw = Float.parseFloat(yawS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid yaw value to deserialize: '"
                    + yawS + "'!", ex);
        }

        float pitch = 0;
        try {
            pitch = Float.parseFloat(pitchS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid pitch value to deserialize: '"
                    + pitchS + "'!", ex);
        }

        Location loc = new Location(world, x, y, z, yaw, pitch);
        return loc;
    }

}
