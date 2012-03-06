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

        String msg = MsgColor.CHAT_CITY + "[C] " + govPref + ChatColor.WHITE + player.
                getDisplayName() + ": " + message;

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
