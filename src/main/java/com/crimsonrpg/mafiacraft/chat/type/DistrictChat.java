/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat.type;

import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
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
		
		String msg = MsgColor.CHAT_DISTRICT + "[D] " + ChatColor.WHITE + player.getDisplayName() + ": " + message;
		
        for (MPlayer players : player.getDistrict().getPlayers()) {
            players.sendMessage(msg);
        }
    }

    @Override
    public String getName(MPlayer player) {
        return "district";
    }

    @Override
    public boolean canJoin(MPlayer player) {
        if (player.getDistrict() == null) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "District";
    }
}
