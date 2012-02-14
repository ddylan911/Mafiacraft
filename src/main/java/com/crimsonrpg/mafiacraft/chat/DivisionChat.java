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
public class DivisionChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        if (player.getDivision() == null) {
            player.sendMessage(ChatColor.RED + "You are not in a division.");
            return;
        }
        for (MPlayer players : player.getDivision().getOnlineMembers()) {
            players.sendMessage("[" + player.getDivision().getPrefix() + "]" +  player.getDisplayName() + " " + message);
        }
    }
}