/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

/**
 * Represents a type of district.
 */
public enum DistrictType {
    CONTESTED(true, true, true, true),
    ANARCHIC(true, true, false, true),
    LAWLESS(true, false, true, true),
    SHARED(false, true, false, true),
    OPEN(false, true, true, true),
    GOVERNMENT(false, false, false, true),
    RESERVED(false, false, false, false),
    UNEXPLORED(false, false, false, false);

    private boolean pvp;

    private boolean buildAnywhere;

    private boolean claim;

    private boolean enter;

    private DistrictType(boolean pvp, boolean build, boolean claim, boolean enter) {
        this.pvp = pvp;
        this.buildAnywhere = build;
        this.claim = claim;
        this.enter = enter;
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

    public boolean canEnter() {
        return enter;
    }

}
