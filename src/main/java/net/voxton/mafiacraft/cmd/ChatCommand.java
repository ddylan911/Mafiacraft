/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.chat.ChatType;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /chat command.
 */
public final class ChatCommand {

    public static void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR
                    + "Sorry, this command is only usable in game.");
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) sender);

        if (args.length < 1) {
            doChat(player);
            return;
        }

        String result = doChat(player, args[0]);
        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doChat(MPlayer player) {
        doChat(player, "");
        return null;
    }

    public static String doChat(MPlayer player, String type) {
        if (!player.hasPermission("mafiacraft.visitor")) {
            return "You aren't allowed to use this command.";
        }

        ChatType chatType = ChatType.valueOf(type);
        if (chatType == null) {
            return "Invalid chat type given.";
        }

        if (!chatType.canJoin(player)) {
            return "You are not allowed to be part of that chat.";
        }

        player.setChatType(chatType);
        player.sendMessage(MsgColor.SUCCESS + "You have changed to " + chatType.
                getName(player) + " chat.");
        return null;
    }

}
