/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.player.MsgColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Mafiacraft command. Very meta.
 */
public class MafiacraftCommand {
    /**
     * Parses the command and executes it.
     * 
     * @param sender The command sender.
     * @param cmd The command.
     * @param label The command label.
     * @param args 
     */
    public static void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (!sender.hasPermission("mafiacraft.admin")) {
            sender.sendMessage(MsgColor.ERROR
                    + "You are not allowed to use this command.");
        }
        
        
    }

}
