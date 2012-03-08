/*
 * This file is part of Mafiacraft.
 * 
 * Mafiacraft is released under the Voxton License version 1.
 *
 * Mafiacraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition to this, you must also specify that this product includes 
 * software developed by Voxton.net and may not remove any code
 * referencing Voxton.net directly or indirectly.
 * 
 * Mafiacraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
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
        String msg = pref + " " + ChatColor.WHITE + player.getDisplayName()
                + ": " + message;

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