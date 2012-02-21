/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Dylan
 */
public class AdminChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!players.hasPermission("mc.admin.chat")) {
                continue;
            }
            players.sendMessage(MsgColor.CHAT_ADMIN + "[A]" + ChatColor.WHITE + player.getDisplayName() + ": " + message);
        }
    }

    @Override
    public boolean canJoin(MPlayer player) {
        return player.getBukkitEntity().hasPermission("mc.admin.chat");
    }

    @Override
    public String getName(MPlayer player) {
        return "admin";
    }

    @Override
    public String getName() {
        return "admin";
    }
}