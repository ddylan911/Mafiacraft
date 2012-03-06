/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.chat.type;

import net.voxton.mafiacraft.chat.ChatType;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.player.MPlayer;
import org.bukkit.ChatColor;

/**
 *
 * @author Dylan
 */
public class OfficerChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        String pref = player.getGovernment().getType().getLocale("off.chatp");
        String msg = pref + " " + ChatColor.WHITE + player.getDisplayName() + ": " + message;

        for (MPlayer o : player.getGovernment().getOnlineOfficers()) {
            o.sendMessage(msg);
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