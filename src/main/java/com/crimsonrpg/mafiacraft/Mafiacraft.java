/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

import com.crimsonrpg.mafiacraft.player.MPlayer;
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class Mafiacraft {
    private static MafiacraftPlugin plugin;
    
    public static void setPlugin(MafiacraftPlugin mcp) {
        if (Mafiacraft.plugin == null) {
            Mafiacraft.plugin = mcp;
        }
    }

    public static MafiacraftPlugin getPlugin() {
        return plugin;
    }
    
    /**
     * Gets an MPlayer from a Player.
     * 
     * @param player
     * @return 
     */
    public static MPlayer getPlayer(Player player) {
        return getPlugin().getPlayerManager().getPlayer(player);
    }

    /**
     * Returns a list of all MPlayers currently online the server.
     * 
     * @return 
     */
    public static List<MPlayer> getOnlinePlayers() {
        return getPlugin().getPlayerManager().getPlayerList();
    }
}
