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
package net.voxton.mafiacraft.core.city;

import net.voxton.mafiacraft.core.geo.Section;

/**
 * Represents an entity that can both own and purchase land.
 */
public interface LandPurchaser extends LandOwner {

    /**
     * Gets the amount of land the landowner owns.
     *
     * @return
     */
    public int getLand();

    /**
     * Sets the amount of land.
     *
     * @param amt
     * @return
     */
    public LandOwner setLand(int amt);

    /**
     * Increments the amount of land.
     *
     * @return
     */
    public LandOwner incLand();

    /**
     * Decrements the amount of land.
     *
     * @return
     */
    public LandOwner decLand();

    /**
     * Claims the given section.
     *
     * @param section
     * @return
     */
    public LandOwner claim(Section section);

    /**
     * Unclaims the given section.
     *
     * @param section
     * @return
     */
    public LandOwner unclaim(Section section);

}
