/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.Mafiacraft;
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
     * @param args The arguments.
     */
    public static void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args) {

        if (args.length < 1) {
            doHelp(sender);
        }

        String function = args[0];

        if (function.equalsIgnoreCase("reload")) {
            doReload(sender);
        } else if (function.equalsIgnoreCase("saveall")) {
            doSaveAll(sender);
        } else if (function.equalsIgnoreCase("version")) {
            doVersion(sender);
        } else {
            doHelp(sender);
        }
    }

    public static String doHelp(CommandSender sender) {
        sender.sendMessage(MsgColor.INFO
                + "Help: /mafiacraft <version|reload|saveall>. Nuff said.");
        return null;
    }

    public static String doReload(CommandSender sender) {
        if (!sender.hasPermission("mafiacraft.admin")) {
            sender.sendMessage(MsgColor.ERROR
                    + "You are not allowed to use this command.");
        }

        sender.sendMessage(MsgColor.INFO + "Mafiacraft reloading...");
        Mafiacraft.loadAll();
        sender.sendMessage(MsgColor.SUCCESS + "Mafiacraft reload complete.");
        return null;
    }

    public static String doSaveAll(CommandSender sender) {
        if (!sender.hasPermission("mafiacraft.admin")) {
            sender.sendMessage(MsgColor.ERROR
                    + "You are not allowed to use this command.");
        }

        sender.sendMessage(MsgColor.SUCCESS + "Mafiacraft saving all...");
        Mafiacraft.saveAll();
        sender.sendMessage(MsgColor.SUCCESS + "Mafiacraft saveAll complete.");
        return null;
    }

    public static String doVersion(CommandSender sender) {
        sender.sendMessage(MsgColor.INFO
                + "This server is running Mafiacraft version " + Mafiacraft.
                getPlugin().getVersionDetailed() + ".");
        return null;
    }

}
