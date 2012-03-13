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

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.chat.ChatType;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.voxton.mafiacraft.locale.Locale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /chat command.
 */
public final class ChatActions {

    public  void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR + Mafiacraft.getDefaultLocale().localize(
                    "command.general.ingame-only"));
            return;
        }

        MPlayer player = Mafiacraft.getPlayer(((Player) sender).getName());

        if (args.length < 1) {
            doChat(player);
            return;
        }

        String result = doChat(player, args[0]);
        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public  String doChat(MPlayer player) {
        doChat(player, "");
        return null;
    }

    public  String doChat(MPlayer player, String type) {
        ChatType chatType = ChatType.valueOf(type);
        if (chatType == null) {
            return player.getLocale().localize("command.chat.invalid-chat-type");
        }

        if (!chatType.canJoin(player)) {
            return player.getLocale().localize("command.chat.not-allowed");
        }

        player.setChatType(chatType);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.chat.changed", chatType.getName()));
        return null;
    }

}
