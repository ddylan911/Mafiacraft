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

import net.voxton.mafiacraft.core.city.LandOwner;
import net.voxton.mafiacraft.core.city.District;
import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.util.GeoUtils;

/**
 * Represents a section of land.
 */
public class Section {

    public static final int HEIGHT_BITS = 8;

    public static final int SIDE_BITS = 4;

    public static final int HEIGHT_MASK = (~0) >>> (Integer.SIZE - HEIGHT_BITS);

    public static final int SIDE_MASK = (~0) >>> (Integer.SIZE - SIDE_BITS);

    public static final int HEIGHT_LENGTH = HEIGHT_MASK + 1;

    public static final int SIDE_LENGTH = SIDE_MASK + 1;

    private final MWorld world;

    private final int x;

    private final int y;

    private final int z;

    public Section(MWorld world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MWorld getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getDistrictX() {
        return x >> District.SIDE_BITS;
    }

    public int getDistrictY() {
        return y >> District.HEIGHT_BITS;
    }

    public int getDistrictZ() {
        return z >> District.SIDE_BITS;
    }

    public int getOriginX() {
        return x & ~SIDE_MASK;
    }

    public int getOriginY() {
        return y & ~HEIGHT_MASK;
    }

    public int getOriginZ() {
        return z & ~SIDE_MASK;
    }

    public District getDistrict() {
        return Mafiacraft.getCityManager().getDistrict(getWorld(),
                getDistrictX(), getDistrictZ());
    }

    public LandOwner getOwner() {
        return getDistrict().getOwner(this);
    }

    public byte getIdWithinDistrict() {
        return GeoUtils.coordsToSectionId(x, z);
    }

    public String getName() {
        District d = getDistrict();
        StringBuilder nameBuilder = new StringBuilder(d.getName()).append('-');
        byte sid = getIdWithinDistrict();
        nameBuilder.append(Byte.toString(sid));
        return nameBuilder.toString();
    }

}
