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
package net.voxton.mafiacraft.core.chat.type;

import net.voxton.mafiacraft.core.chat.ChatType;
import net.voxton.mafiacraft.core.gov.Government;
import net.voxton.mafiacraft.core.player.MPlayer;
import net.voxton.mafiacraft.core.chat.MsgColor;

/**
 * District chat channel.
 */
public class DistrictChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        if (player.getDistrict() == null) {
            player.sendMessage(MsgColor.ERROR + "You are not in a district."); //Why
            return;
        }

        String govPref = "";
        Government gov = player.getGovernment();
        if (gov != null) {
            govPref = gov.getChatPrefix() + " ";
        }

        String msg = MsgColor.CHAT_DISTRICT + "[D] " + govPref + MsgColor.NORMAL
                + player.getDisplayName() + ": " + message;

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
