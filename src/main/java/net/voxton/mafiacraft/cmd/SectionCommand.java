/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commands related to working with sections.
 */
public final class SectionCommand {

    public static void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR
                    + "Sorry, this command is only usable in game.");
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) sender);

        if (args.length < 1) {
            doHelp(player);
            return;
        }

        //Get the function we want to do.
        String function = args[0];
        List<String> largs = new ArrayList<String>(Arrays.asList(args));
        largs.remove(0);

        String result = null;
        if (largs.size() < 1) {
            if (function.equalsIgnoreCase("info")) {
                result = doInfo(player);
            } else {
                result = doHelp(player);
            }
        } else {
            result = doHelp(player);
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doHelp(MPlayer player) {
        //TODO: help
        player.sendMessage(MsgColor.ERROR + "TROLOLOLOLOL");
        return null;
    }

    public static String doInfo(MPlayer player) {
        //TODO: info
        player.sendMessage(MsgColor.ERROR + "TROLOLOLOLOLO");
        return null;
    }

}
