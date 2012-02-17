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
public class CityChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        if (player.getCity() == null) {
            player.sendMessage(ChatColor.RED + "You are not in a city.");
            return;
        }
        for (MPlayer players : player.getCity().getPlayers()) {
            players.sendMessage(ChatColor.RED + "[C]" + ChatColor.WHITE + player.getDisplayName() + ": " + message);
        }
    }

	@Override
	public String getName(MPlayer player) {
		return "city";
	}

    @Override
    public boolean canJoin(MPlayer player) {
        return true;
    }
}
