/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat.type;

import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import org.bukkit.ChatColor;

/**
 * Government chat type.
 */
public class GovernmentChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
		String pref = player.getGovernment().getType().getLocale("gov.chatp");
		String msg = pref + " " + ChatColor.WHITE + player.getDisplayName() + ": " + message;
		
        for (MPlayer p : player.getGovernment().getOnlineMembers()) {
           p.sendMessage(msg);
        }
    }

    @Override
    public String getName(MPlayer player) {
        return player.getGovernment().getType().getName();
    }

    @Override
    public boolean canJoin(MPlayer player) {
        return player.getGovernment() != null;
    }
}