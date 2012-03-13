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
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.chat.type;

import net.voxton.mafiacraft.chat.ChatType;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;

/**
 * Division chat channel.
 */
public class DivisionChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        String pref = player.getGovernment().getType().getLocale("div.chatp");
        String msg = pref + " " + MsgColor.NORMAL + player.getDisplayName()
                + ": " + message;

        for (MPlayer p : player.getDivision().getOnlineMembers()) {
            p.sendMessage(msg);
        }
    }

    @Override
    public String getName(MPlayer player) {
        Government gov = player.getGovernment();
        String div = (gov == null) ? "division" : gov.getType().getLocale(
                "division");
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