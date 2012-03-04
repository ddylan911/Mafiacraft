/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.geo;

import net.voxton.mafiacraft.player.MPlayer;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a type of district.
 */
public enum DistrictType {
    /**
     * PvP enabled shared zone. Do whatever the crap you want.
     */
    ANARCHIC(true, true, false, false, true),
    /**
     * Mafias can do whatever they want.
     */
    CONTESTED(true, false, false, true, true),
    /**
     * Goverment district with PvP enabled. Good for arenas or some sort of
     * shady RP alley thing.
     */
    LAWLESS(true, false, true, true, true),
    /**
     * People cannot claim sections of these districts. These districts can only
     * be broken. Admins will likely regen them. This is for scrubs who just
     * join and need some materials.
     */
    SHARED(false, true, false, false, true),
    /**
     * People can do anything they want in these districts. This is the default
     * zoning of a district. Individuals can purchase sections for themselves.
     * Mafias cannot purchase land here, though. No PvP.
     */
    OPEN(false, false, false, true, true),
    /**
     * Trade districts are open districts that are generally closer to spawn.
     * They are only open to donators to the server.
     */
    TRADE(false, false, false, true, true),
    /**
     * City government, cannot be built in or claimed. Peaceful, no PvP.
     */
    GOVERNMENT(false, false, true, false, true),
    /**
     * You cannot walk into these districts. Admins can worldedit, build, and
     * crap here, though. Mayors can walk into these too to claim them.
     */
    RESERVED(false, false, false, false, false),
    /**
     * Basically a reserved district with a different name.
     */
    UNEXPLORED(false, false, false, false, false);

    private boolean pvp;

    private boolean build;

    private boolean government;

    private boolean claim;

    private boolean enter;

    private DistrictType(boolean pvp, boolean build, boolean government, boolean claim, boolean enter) {
        this.pvp = pvp;
        this.build = build;
        this.government = government;
        this.claim = claim;
        this.enter = enter;
    }

    /**
     * Returns true if you can build in unclaimed land.
     *
     * @return
     */
    public boolean canBuild() {
        return build;
    }

    /**
     * Returns true if the district is only modifiable by the government.
     *
     * @return
     */
    public boolean isGovernment() {
        return government;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isClaim() {
        return claim;
    }

    public boolean canEnter(MPlayer player) {
        if (!enter) {
            return (player.isAMayor());
        }
        return true;
    }

    /**
     * Gets a player-friendly name of the type of district.
     *
     * @return
     */
    public String niceName() {
        return name().toLowerCase();
    }

    private static Map<String, DistrictType> typeMap;

    /**
     * Retrieves a district type from a string.
     *
     * @param typeString
     * @return
     */
    public static DistrictType fromString(String typeString) {
        return typeMap.get(typeString.toLowerCase());
    }

    static {
        typeMap = new HashMap<String, DistrictType>();

        for (DistrictType type : DistrictType.values()) {
            typeMap.put(type.name().toLowerCase(), type);
        }

        typeMap.put("s", DistrictType.SHARED);
        typeMap.put("c", DistrictType.CONTESTED);
        typeMap.put("a", DistrictType.ANARCHIC);
        typeMap.put("r", DistrictType.RESERVED);
        typeMap.put("l", DistrictType.LAWLESS);
        typeMap.put("o", DistrictType.OPEN);
        typeMap.put("g", DistrictType.GOVERNMENT);
        typeMap.put("u", DistrictType.UNEXPLORED);
    }

}
