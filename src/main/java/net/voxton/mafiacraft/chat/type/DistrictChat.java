/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.chat.type;

import net.voxton.mafiacraft.chat.ChatType;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import org.bukkit.ChatColor;

/**
 *
 * @author Dylan
 */
public class DistrictChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        if (player.getDistrict() == null) {
            player.sendMessage(ChatColor.RED + "You are not in a district."); //Why
            return;
        }

        String govPref = "";
        Government gov = player.getGovernment();
        if (gov != null) {
            govPref = gov.getChatPrefix() + " ";
        }

        String msg = MsgColor.CHAT_DISTRICT + "[D] " + govPref + ChatColor.WHITE + player.
                getDisplayName() + ": " + message;

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
