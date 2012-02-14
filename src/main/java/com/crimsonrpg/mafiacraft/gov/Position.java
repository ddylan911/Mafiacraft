/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

/**
 * Represents a position within the government.
 */
public enum Position {
    NONE(-1, false),
    AFFILIATE(-1, false),
    WORKER(-1, true),
    MANAGER(-1, true),
    OFFICER(10, false),
    VICE_LEADER(1, false),
    LEADER(1, false);

    private int limit;

    private boolean division;

    private Position(int limit, boolean division) {
        this.limit = limit;
        this.division = division;
    }

    public int getLimit(Government government) {
        if (limit > 0) {
            return limit;
        }
        //TODO: write size checking
        return 100;
    }

    public boolean isDivision() {
        return division;
    }

}
