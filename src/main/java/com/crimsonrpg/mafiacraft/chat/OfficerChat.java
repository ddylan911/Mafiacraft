/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import org.bukkit.ChatColor;

/**
 *
 * @author Dylan
 */
public class OfficerChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        for (MPlayer officers : player.getGovernment().getOnlineOfficers()) {
            officers.sendMessage(ChatColor.GRAY + "[O]" + ChatColor.WHITE + player.getDisplayName() + ": " + message);
        }
    }

    @Override
    public String getName(MPlayer player) {
        return player.getGovernment().getType().getLocale("officer");
    }

    @Override
    public boolean canJoin(MPlayer player) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return false;
        }
        return gov.getOfficers().contains(player.getName());
    }

    @Override
    public String getName() {
        return "officer";
    }
}