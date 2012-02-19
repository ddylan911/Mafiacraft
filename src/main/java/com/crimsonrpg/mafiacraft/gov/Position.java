/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

/**
 * Represents a position within the government.
 */
public enum Position {
    NONE(-2, false, 0),
    AFFILIATE(-2, false, 0),
    WORKER(-1, true, 0),
    MANAGER(-1, true, 0),
    OFFICER(10, false, 0),
    VICE_LEADER(1, false, 1),
    LEADER(1, false, 1);

    private int limit;

    private boolean division;

    private int minimum;

    private Position(int limit, boolean division, int minimum) {
        this.limit = limit;
        this.division = division;
        this.minimum = minimum;
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
        return 100;
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

}
