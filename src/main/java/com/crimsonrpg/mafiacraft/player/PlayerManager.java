/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class PlayerManager {
    private Map<Player, MPlayer> mplayers = new HashMap<Player, MPlayer>();

    private final Mafiacraft mc;

    public PlayerManager(Mafiacraft mc) {
        this.mc = mc;
    }

    /**
     * Gets a list of all players.
     * 
     * @return 
     */
    public List<MPlayer> getPlayerList() {
        List<MPlayer> players = new ArrayList<MPlayer>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(getPlayer(player));
        }
        return players;
    }
    
    /**
     * Gets a player from a Bukkit player.
     * 
     * @param player
     * @return 
     */
    public MPlayer getPlayer(Player player) {
        MPlayer mplayer = mplayers.get(player);
        if (mplayer == null) {
            mplayer = loadPlayer(player);
        }
        return mplayer;
    }

    /**
     * Loads a player.
     * 
     * @param player
     * @return 
     */
    public MPlayer loadPlayer(Player player) {
        MPlayer mplayer = new MPlayer(player);
        mplayers.put(player, mplayer);
        return mplayer;
    }

}
