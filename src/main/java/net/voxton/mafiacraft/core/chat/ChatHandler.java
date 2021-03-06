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
package net.voxton.mafiacraft.core.chat;

import net.voxton.mafiacraft.core.MafiacraftCore;
import net.voxton.mafiacraft.core.gov.GovType;
import net.voxton.mafiacraft.core.gov.Government;
import net.voxton.mafiacraft.core.gov.Position;
import net.voxton.mafiacraft.core.player.MPlayer;

/**
 * Handles chat.
 */
public class ChatHandler {

    private MafiacraftCore mc;

    public ChatHandler(MafiacraftCore mc) {
        this.mc = mc;
    }

    /**
     * Takes in a MPlayer, and a Message, and sends the
     *
     * @param message to all the players in that
     * @param player's government.
     * @param player
     * @param message
     */
    public void handleMessage(MPlayer player, String message) {
        updateDisplayName(player);
        player.getChatType().chat(player, message);
    }

    /**
     * Updates a player's display name.
     *
     * @param player The player to update.
     */
    public void updateDisplayName(MPlayer player) {
        String display = player.getName();

        //TODO: Do donator colors.

        //Do don title if exists.
        Government gov = player.getGovernment();
        if (gov != null && player.getPosition().equals(Position.LEADER)) {
            GovType type = gov.getType();
            if (type.equals(GovType.MAFIA)) {
                display = "Don " + display;
            } else if (type.equals(GovType.POLICE)) {
                display = "Chief " + display;
            }
        }

        player.setDisplayName(display);
    }

}
