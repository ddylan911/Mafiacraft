/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.geo;

import org.bukkit.Chunk;

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
     * Claims the given chunk.
     *
     * @param chunk
     * @return
     */
    public LandOwner claim(Chunk chunk);

    /**
     * Unclaims the given chunk.
     *
     * @param chunk
     * @return
     */
    public LandOwner unclaim(Chunk chunk);

}
