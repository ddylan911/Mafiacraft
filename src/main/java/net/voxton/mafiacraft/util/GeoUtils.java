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
        x += 0x8000;
        z += 0x8000;
        return ((x & 0xffff) << 16) | (z & 0xffff);
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
