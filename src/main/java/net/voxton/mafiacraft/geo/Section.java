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
package net.voxton.mafiacraft.geo;

import net.voxton.mafiacraft.Mafiacraft;

/**
 * Represents a section of land.
 */
public class Section {

    public static final int HEIGHT_BITS = 8;

    public static final int SIDE_BITS = 4;
    
    public static final int HEIGHT_MASK  = (~0) >>> (Integer.SIZE - HEIGHT_BITS);
    
    public static final int SIDE_MASK = (~0) >>> (Integer.SIZE - SIDE_BITS);
    
    public static final int HEIGHT_LENGTH = HEIGHT_MASK + 1;
    
    public static final int SIDE_LENGTH = SIDE_MASK + 1;

    private final CityWorld world;

    private final int x;

    private final int z;

    public Section(CityWorld world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public CityWorld getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getDistrictX() {
        return x >> District.SIDE_BITS;
    }

    public int getDistrictZ() {
        return z >> District.SIDE_BITS;
    }
    
    public int getOriginX() {
        return getSectionOrigin(x);
    }
    
    public int getOriginZ() {
        return getSectionOrigin(z);
    }

    public District getDistrict() {
        return Mafiacraft.getCityManager().getDistrict(this);
    }

    /**
     * Gets the origin of a section in relation to a district.
     * 
     * @param coord
     * @return 
     */
    private static int getSectionOrigin(int coord) {
        return (coord >= 0) ? (coord & ~SIDE_MASK) : -((Math.abs(coord + 1) & ~SIDE_MASK)
                + SIDE_LENGTH);
    }
}
