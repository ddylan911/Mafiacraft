/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.player.MPlayer;
import org.bukkit.Chunk;

/**
 *
 * @author simplyianm
 */
public class Government implements LandOwner {
    private final int id;

    private String name;

    public Government(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Government setName(String name) {
        this.name = name;
        return this;
    }

    public boolean canBuild(MPlayer player, Chunk c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getOwnerName() {
        return name;
    }

}
