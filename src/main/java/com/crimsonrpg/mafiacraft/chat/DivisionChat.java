/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.gov.GovType;
import com.crimsonrpg.mafiacraft.gov.Government;
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
            if (player.getDivision().getGovernment().getType().equals(GovType.MAFIA)) {
                players.sendMessage(ChatColor.GOLD + "[R]" + ChatColor.WHITE + player.getDisplayName() + ": " + message);
                continue;
            }
            players.sendMessage(ChatColor.AQUA + "[S]" + ChatColor.WHITE + player.getDisplayName() + ": " + message);
        }
    }

	@Override
	public String getName(MPlayer player) {
		Government gov = player.getGovernment();
		String div = (gov == null) ? "division" : gov.getType().getLocale("division");
		return div;
	}

    @Override
    public boolean canJoin(MPlayer player) {
        if (player.getDivision() == null) {
            return false;
        }
        return true;
    }
}