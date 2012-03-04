/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

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
        return compareTo(other) >= 0;
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
