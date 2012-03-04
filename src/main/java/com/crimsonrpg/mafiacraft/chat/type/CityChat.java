/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat.type;

import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import org.bukkit.ChatColor;

/**
 *
 * @author Dylan
 */
public class CityChat extends ChatType {
    @Override
    public void chat(MPlayer player, String message) {
        if (player.getCity() == null) {
            player.setChatType(ChatType.GLOBAL);
            player.sendMessage(ChatColor.RED + "You are not in a city; you have been moved to global chat.");
            return;
        }

        String govPref = "";
        Government gov = player.getGovernment();
        if (gov != null) {
            govPref = gov.getChatPrefix() + " ";
        }

        String msg = MsgColor.CHAT_CITY + "[C] " + govPref + ChatColor.WHITE + player.getDisplayName() + ": " + message;

        for (MPlayer players : player.getCity().getPlayers()) {
            players.sendMessage(msg);
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

    @Override
    public String getName() {
        return "city";
    }

}
