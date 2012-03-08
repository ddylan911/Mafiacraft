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
package net.voxton.mafiacraft.gov;

/**
 * Represents a position within the government.
 */
public enum Position {

    LEADER(1, false, 1, 15),
    VICE_LEADER(1, false, 1, 12),
    OFFICER(10, false, 0, 10),
    MANAGER(-1, true, 0, 5),
    WORKER(-1, true, 0, 2),
    AFFILIATE(-2, false, 0, 0),
    NONE(-2, false, 0, 0);

    private int limit;

    private boolean division;

    private int minimum;

    private int killCost;

    private Position(int limit, boolean division, int minimum, int killCost) {
        this.limit = limit;
        this.division = division;
        this.minimum = minimum;
        this.killCost = killCost;
    }

    /**
     * Gets the minimum amount of players in this government position.
     *
     * @param government
     * @return
     */
    public int getMinimum(Government government) {
        return minimum;
    }

    /**
     * Gets the limit of the position if there is a government.
     *
     * @param government
     * @return
     */
    public int getLimit(Government government) {
        if (limit > 0) {
            return limit;
        }

        if (limit == -2) {
            return 1000000; //Unlimited players
        }

        //TODO: write size checking for the worker/manager pizzazz
        return 1000000;
    }

    /**
     * Returns true if this position is associated with divisions.
     *
     * @return
     */
    public boolean isDivision() {
        return division;
    }

    /**
     * Returns true if the position is at least the other one.
     *
     * @param other
     * @return
     */
    public boolean isAtLeast(Position other) {
        return compareTo(other) <= 0;
    }

    /**
     * Gets the cost of killing a player with this position.
     *
     * @return
     */
    public int getKillCost() {
        return killCost;
    }

}
