/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
