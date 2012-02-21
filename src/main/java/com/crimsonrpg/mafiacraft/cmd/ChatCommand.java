/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /chat command.
 */
public class ChatCommand {
    public static void parseCmd(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR + "Sorry, this command is only usable in game.");
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) sender);

        if (args.length < 1) {
            doChat(player);
            return;
        }

       String  result = doChat(player, args[0]);
       if (result != null) {
           player.sendMessage(MsgColor.ERROR + result);
       }
    }

    public static String doChat(MPlayer player) {
        doChat(player, "");
        return null;
    }

    public static String doChat(MPlayer player, String type) {
        ChatType chatType = ChatType.valueOf(type);
        if (chatType == null) {
            return "Invalid chat type given.";
        }

        if (!chatType.canJoin(player)) {
            return "You are not allowed to be part of that chat.";
        }

        player.setChatType(chatType);
        player.sendMessage(MsgColor.SUCCESS + "You have changed to " + chatType.getName(player) + " chat.");
        return null;
    }

}
