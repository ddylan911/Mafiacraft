/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

/**
 * Represents a type of district.
 */
public enum DistrictType {
    CONTESTED(true, true),
    ANARCHIC(true, true),
    LAWLESS(true, false),
    SHARED(false, true),
    OPEN(false, true),
    RESERVED(false, false),
    GOVERNMENT(false, false);

    private boolean pvp;

    private boolean build;

    private DistrictType(boolean pvp, boolean build) {
        this.pvp = pvp;
        this.build = build;
    }

    public boolean isBuild() {
        return build;
    }

    public boolean isPvp() {
        return pvp;
    }

}
