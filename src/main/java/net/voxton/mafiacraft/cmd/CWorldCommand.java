/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.CityWorld;
import net.voxton.mafiacraft.geo.WorldToggle;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class CWorldCommand {

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
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("toggle")) {
                result = doToggle(player, largs.get(0));
            } else {
                result = doHelp(player);
            }
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doHelp(MPlayer player) {
        //TODO: help
        player.sendMessage(MsgColor.INFO + "Todo: help");
        return null;
    }

    public static String doToggle(MPlayer player, String toggle) {
        if (!player.hasPermission("mafiacraft.admin")) {
            return "You aren't allowed to use this command.";
        }

        CityWorld world = player.getCityWorld();

        WorldToggle tog = null;
        try {
            tog = WorldToggle.valueOf(toggle.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return "Invalid toggle name. Available toggles are: " + Arrays.
                    asList(WorldToggle.values());
        }

        boolean val = world.toggle(tog);
        player.sendMessage(MsgColor.SUCCESS + "The toggle named '" + tog.
                toString() + "' has been set to " + val + ".");
        return null;
    }

}
