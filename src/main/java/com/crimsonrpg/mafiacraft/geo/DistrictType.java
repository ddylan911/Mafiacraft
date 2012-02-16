/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

/**
 * Represents a type of district.
 */
public enum DistrictType {
    CONTESTED(true, true, true),
    ANARCHIC(true, true, false),
    LAWLESS(true, false, true),
    SHARED(false, true, false),
    OPEN(false, true, true),
    RESERVED(false, false, false),
    GOVERNMENT(false, false, false);

    private boolean pvp;

    private boolean buildAnywhere;

    private boolean claim;

    private DistrictType(boolean pvp, boolean build, boolean claim) {
        this.pvp = pvp;
        this.buildAnywhere = build;
        this.claim = claim;
    }

    public boolean canBuildAnywhere() {
        return buildAnywhere;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isClaim() {
        return claim;
    }

}
