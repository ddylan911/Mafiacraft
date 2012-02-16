/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.player.MPlayer;
import org.bukkit.ChatColor;

/**
 *
 * @author Dylan
 */
public class DistrictChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        if (player.getDistrict() == null) {
            player.sendMessage(ChatColor.RED + "You are not in a district.");
            return;
        }
        for (MPlayer players : player.getDistrict().getPlayers()) {
            players.sendMessage(ChatColor.BLUE + "[D]" + ChatColor.WHITE + player.getDisplayName() + ": " + message);
        }
    }
}
