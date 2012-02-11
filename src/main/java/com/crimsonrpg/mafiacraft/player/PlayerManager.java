/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import java.util.HashMap;
import java.util.Map;
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

    public MPlayer getPlayer(Player player) {
        MPlayer mplayer = mplayers.get(player);
        if (mplayer == null) {
            mplayer = loadPlayer(player);
        }
        return mplayer;
    }

    public MPlayer loadPlayer(Player player) {
        MPlayer mplayer = new MPlayer(player);
        mplayers.put(player, mplayer);
        return mplayer;
    }

}
