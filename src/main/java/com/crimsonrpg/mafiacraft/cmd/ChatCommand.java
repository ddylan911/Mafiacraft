/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /chat command.
 */
public class ChatCommand {

    public static void parseCmd(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR + "You cannot use this command from console.");
            return;
        }
        MPlayer player = Mafiacraft.getPlayer((Player) sender);
        ChatType chatType = ChatType.valueOf(args[0]);
        if (chatType == null) {
            player.sendMessage(MsgColor.ERROR + "Invalid Chat.");
            return;
        }
        if (chatType.canJoin(player)) {
            player.setChatType(chatType);
            player.sendMessage(MsgColor.SUCCESS + "You have joined the chat " + chatType.getName() + ".");
        }
    }

    public static String doChat(MPlayer player) {
        doChat(player, ChatType.DEFAULT);
        return null;
    }

    public static String doChat(MPlayer player, ChatType type) {
        player.setChatType(type);
        player.sendMessage(MsgColor.SUCCESS + "You have changed to " + type.getName(player) + " chat.");
        return null;
    }
}
