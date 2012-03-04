/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat.type;

import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Global chat.
 */
public class GlobalChat extends ChatType {
    @Override
    public void chat(MPlayer player, String message) {
        String govPref = "";
        Government gov = player.getGovernment();
        if (gov != null) {
            govPref = gov.getChatPrefix() + " ";
        }

        String msg = MsgColor.CHAT_GLOBAL + "[G] " + govPref + ChatColor.WHITE + player.getDisplayName() + ": " + message;

        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(msg);
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

    @Override
    public String getName() {
        return "global";
    }

}
