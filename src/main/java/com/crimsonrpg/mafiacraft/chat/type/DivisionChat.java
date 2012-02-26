/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat.type;

import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.gov.GovType;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import org.bukkit.ChatColor;

/**
 *
 * @author Dylan
 */
public class DivisionChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        String pref = player.getGovernment().getType().getLocale("div.chatp");
		String msg = pref + " " + ChatColor.WHITE + player.getDisplayName() + ": " + message;
		
        for (MPlayer p : player.getDivision().getOnlineMembers()) {
            p.sendMessage(msg);
        }
    }

    @Override
    public String getName(MPlayer player) {
        Government gov = player.getGovernment();
        String div = (gov == null) ? "division" : gov.getType().getLocale("division");
        return div;
    }

    @Override
    public boolean canJoin(MPlayer player) {
        if (player.getDivision() == null) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "division";
    }
}