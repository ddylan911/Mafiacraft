/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.gov.GovType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Dylan
 */
public class ChatCommand {

    public void execute(CommandSender sender, Command cmd, String label, String[] args, GovType type) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The person who made this plugin is too lazy to make them work in the console.");
            return;
        }
        MPlayer player = Mafiacraft.getPlayer((Player) sender);
        if (args.length < 1) {
            player.setChatType(ChatType.GLOBAL);
            player.sendMessage(ChatColor.GREEN + "You have been set to Global chat.");
            return;
        }
        if (args[0].equalsIgnoreCase("city")) {
            player.sendMessage(ChatColor.GREEN + "You have been moved to City chat.");
            player.setChatType(ChatType.CITY);
            return;
        } else if (args[0].equalsIgnoreCase("district")) {
            player.sendMessage(ChatColor.GREEN + "You have been moved to District chat.");
            player.setChatType(ChatType.DISTRICT);
            return;
        } else if (args[0].equalsIgnoreCase("division")) {
            player.sendMessage(ChatColor.GREEN + "You have been moved to Division chat.");
            player.setChatType(ChatType.DIVISION);
            return;
        } else if (args[0].equalsIgnoreCase("government")) {
            player.sendMessage(ChatColor.GREEN + "You have been moved to Government chat.");
            player.setChatType(ChatType.GOVERNMENT);
            return;
        } else if (args[0].equalsIgnoreCase("Local")) {
            player.sendMessage(ChatColor.GREEN + "You have been moved to Local chat.");
            player.setChatType(ChatType.LOCAL);
            return;
        } else if (args[0].equalsIgnoreCase("Officer")) {
            player.sendMessage(ChatColor.GREEN + "You have been moved to Officer chat.");
            player.setChatType(ChatType.OFFICER);
            return;
        }
    }
}
