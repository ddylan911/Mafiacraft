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
import net.voxton.mafiacraft.core.player.MPlayer;
import net.voxton.mafiacraft.core.chat.MsgColor;

/**
 * Government chat type.
 */
public class GovernmentChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        String pref = player.getGovernment().getType().getLocale("gov.chatp");
        String msg = pref + " " + MsgColor.NORMAL + player.getDisplayName()
                + ": " + message;

        for (MPlayer p : player.getGovernment().getOnlineMembers()) {
            p.sendMessage(msg);
        }
    }

    @Override
    public String getName(MPlayer player) {
        return player.getGovernment().getType().getName();
    }

    @Override
    public boolean canJoin(MPlayer player) {
        return player.getGovernment() != null;
    }

    @Override
    public String getName() {
        return "government";
    }

}