/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.gov.GovType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import org.bukkit.ChatColor;

/**
 *
 * @author Dylan
 */
public class GovernmentChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        for (MPlayer players : player.getGovernment().getOnlineMembers()) {
            if (player.getGovernment().getType().equals(GovType.MAFIA)) {
                players.sendMessage(ChatColor.DARK_RED + "[M]" + ChatColor.WHITE + player.getDisplayName() + " " + message);
            }
            players.sendMessage(ChatColor.DARK_BLUE + "[P]" + ChatColor.WHITE + player.getDisplayName() + " " + message);
        }
    }

	@Override
	public String getName(MPlayer player) {
		return player.getGovernment().getType().getName();
	}
}