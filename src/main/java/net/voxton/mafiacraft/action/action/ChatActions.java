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
package net.voxton.mafiacraft.action.action;

import java.util.List;
import net.voxton.mafiacraft.action.PlayerActions;
import net.voxton.mafiacraft.chat.ChatType;
import net.voxton.mafiacraft.help.HelpMenu;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;

/**
 * /chat command.
 */
public final class ChatActions extends PlayerActions {

    public ChatActions() {
        super();
    }

    @Override
    public String performActionCommand(MPlayer performer, String action,
            List<String> args) {
        if (action.isEmpty()) {
            doChat(performer);
            return null;
        }

        return doChat(performer, action);
    }

    public String doChat(MPlayer player) {
        doChat(player, "");
        return null;
    }

    public String doChat(MPlayer player, String type) {
        ChatType chatType = ChatType.valueOf(type);
        if (chatType == null) {
            return player.getLocale().localize("action.chat.invalid-chat-type");
        }

        if (!chatType.canJoin(player)) {
            return player.getLocale().localize("action.chat.not-allowed");
        }

        player.setChatType(chatType);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.chat.changed", chatType.getName()));
        return null;
    }

}
