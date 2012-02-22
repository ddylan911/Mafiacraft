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
public class LocalChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.getLocation().distanceSquared(player.getBukkitEntity().getLocation()) <= 2500) {
                players.sendMessage(MsgColor.CHAT_LOCAL + "[L] " + ChatColor.WHITE + player.getDisplayName() + ": " + message);
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
