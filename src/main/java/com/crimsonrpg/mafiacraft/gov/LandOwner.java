/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.player.MPlayer;

/**
 * Represents an entity that can own parcels of land. (chunks)
 */
public interface LandOwner {
    public boolean canBuild(MPlayer player);
}
