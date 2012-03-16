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
package net.voxton.mafiacraft.core.geo;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.util.logging.MLogger;
import net.voxton.mafiacraft.core.util.StringSerializer;

/**
 * Represents a point in space.
 */
public class MPoint implements Serializable {

    private transient MWorld world;

    private String worldString;

    private double x;

    private double y;

    private double z;

    public MPoint(MWorld world, double x, double y, double z) {
        this.worldString = world.getName();
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets the world of this CityWorld.
     *
     * @return The world as a CityWorld.
     */
    public MWorld getWorld() {
        if (world == null) {
            world = Mafiacraft.getWorld(worldString);
        }
        return world;
    }

    /**
     * Gets the X coordinate of the MPoint.
     *
     * @return The X coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the Y coordinate of the MPoint.
     *
     * @return The Y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the Z coordinate of the MPoint.
     *
     * @return The Z coordinate.
     */
    public double getZ() {
        return z;
    }

    public int getBlockX() {
        return (int) getX();
    }

    public int getBlockY() {
        return (int) getY();
    }

    public int getBlockZ() {
        return (int) getZ();
    }

    /**
     * Gets the section this MPoint is located.
     *
     * @return The section containing the MPoint.
     */
    public Section getSection() {
        int sx = getBlockX() >> Section.SIDE_BITS;
        int sy = getBlockY() >> Section.HEIGHT_BITS;
        int sz = getBlockZ() >> Section.SIDE_BITS;
        return Mafiacraft.getCityManager().getSection(world, sx, sy, sz);
    }

    /**
     * Gets the distance from the other MPoint squared.
     *
     * @param other
     * @return
     */
    public double distanceSquared(MPoint other) {
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;
        return (dx * dx + dy * dy + dz * dz);
    }

    /**
     * Serializes the MPoint to a string.
     *
     * @return The serialized MPoint.
     */
    public String serializeToString() {
        try {
            return StringSerializer.toString(this);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE, "Could not serialize the MPoint " + this
                    + "!", ex);
        }
        return null;
    }

    /**
     * Deserializes an MPoint from a string.
     *
     * @param string The string to deserialize.
     * @return The deserialized MPoint.
     */
    public static MPoint deserialize(String string) {
        try {
            return StringSerializer.fromString(string, MPoint.class);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE,
                    "Could not deserialize an MPoint from a string!", ex);
        } catch (ClassNotFoundException ex) {
            MLogger.log(Level.SEVERE,
                    "Could not deserialize an MPoint due to a class not being found!",
                    ex);
        } catch (ClassCastException ex) {
            MLogger.log(Level.SEVERE, "Given string is not an MPoint!", ex);
        }
        return null;
    }

}
