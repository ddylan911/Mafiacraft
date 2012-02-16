/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import com.crimsonrpg.mafiacraft.player.MPlayer;
import org.bukkit.Chunk;

/**
 * Represents an entity that can own parcels of land. (chunks)
 */
public interface LandOwner {
    /**
     * Gets the type of owner this LandOwner is.
     *
     * @return
     */
    public OwnerType getOwnerType();

    public String getOwnerName();

    public String getOwnerId();

    /**
     * Returns true if the given player can build in this chunk.
     *
     * @param player
     * @param chunk
     * @return
     */
    public boolean canBuild(MPlayer player, Chunk chunk);

    /**
     * Returns true if the given section can be claimed from this owner.
     *
     * @param chunk
     * @param futureOwner The entity that is trying to claim the land.
     * @return
     */
    public boolean canBeClaimed(Chunk chunk, LandOwner futureOwner);

}