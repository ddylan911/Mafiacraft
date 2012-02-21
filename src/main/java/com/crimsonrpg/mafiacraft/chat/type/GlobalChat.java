/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat.type;

import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
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
            players.sendMessage(MsgColor.CHAT_GLOBAL + "[G] " + ChatColor.WHITE + player.getDisplayName() + ": " + message);
        }
    }

	@Override
	public String getName(MPlayer player) {
		return "global";
	}

    @Override
    public boolean canJoin(MPlayer player) {
        return true;
    }
}
