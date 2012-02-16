/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.player.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Dylan
 */
public class GlobalChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(ChatColor.GREEN + "[G]" + ChatColor.WHITE + player.getDisplayName() + ": " + message);
        }
    }

	@Override
	public String getName(MPlayer player) {
		return "global";
	}
}
