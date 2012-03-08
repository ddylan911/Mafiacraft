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
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.util;

/**
 *
 * @author simplyianm
 */
public class GeoUtils {

    /**
     * Gets the district id from coordinates.
     *
     * @param x
     * @param z
     * @return
     */
    public static int coordsToDistrictId(int x, int z) {
        return (((x + 0x8000) & 0xffff) << 16) | ((z + 0x8000) & 0xffff);
    }

    /**
     * Gets the X of the given district id.
     *
     * @param id The district id.
     * @return The X value.
     */
    public static int xFromDistrictId(int id) {
        return ((id >> 16) & 0xffff) - 0x8000;
    }

    /**
     * Gets the Z of the given district ID.
     *
     * @param id The district id.
     * @return The X value.
     */
    public static int zFromDistrictId(int id) {
        return (id & 0xffff) - 0x8000;
    }

    /**
     * Converts district coordinates to a section ID.
     *
     * <p>Limit is 16.
     *
     * @param x The abscissa 1 - 16
     * @param z The ordinate 1 - 16
     * @return The section id, a byte.
     */
    public static byte coordsToSectionId(int x, int z) {
        return (byte) ((((x - 1) & 0xf) << 4) | ((z - 1) & 0xf));
    }

}
