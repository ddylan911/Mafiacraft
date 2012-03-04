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
 *
 * @author Dylan
 */
public class LocalChat extends ChatType {
    @Override
    public void chat(MPlayer player, String message) {
        String govPref = "";
        Government gov = player.getGovernment();
        if (gov != null) {
            govPref = gov.getChatPrefix() + " ";
        }

        String msg = MsgColor.CHAT_LOCAL + "[L] " + govPref + ChatColor.WHITE + player.getDisplayName() + ": " + message;

        for (Player bPlayer : Bukkit.getOnlinePlayers()) {
            if (bPlayer.getLocation().distanceSquared(bPlayer.getLocation()) <= 2500) {
                bPlayer.sendMessage(msg);
            }
        }
    }

    @Override
    public String getName(MPlayer player) {
        return "local";
    }

    @Override
    public boolean canJoin(MPlayer player) {
        return true;
    }

    @Override
    public String getName() {
        return "local";
    }

}
