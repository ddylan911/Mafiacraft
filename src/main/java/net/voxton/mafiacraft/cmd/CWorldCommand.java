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
import net.voxton.mafiacraft.help.HelpMenu;
import net.voxton.mafiacraft.help.MenuType;
import net.voxton.mafiacraft.util.TPCD;
import net.voxton.mafiacraft.util.TPCD.Type;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Manages the CityWorld.
 */
public final class CWorldCommand {

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
            if (function.equalsIgnoreCase("spawn")) {
                result = doSpawn(player);
            } else {
                result = doHelp(player, function);
            }
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("toggle")) {
                result = doToggle(player, largs.get(0));
            } else if (function.equalsIgnoreCase("help")) {
                result = doHelp(player, largs.get(0));
            } else {
                result = doHelp(player, function);
            }
        } else {
            doHelp(player, function);
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doHelp(MPlayer player) {
        MenuType.CWORLD.sendPage(1, player);
        return null;
    }

    public static String doHelp(MPlayer player, String arg) {
        int page = -1;
        try {
            page = Integer.parseInt(arg);
        } catch (NumberFormatException ex) {
        }

        HelpMenu menu = MenuType.CWORLD;

        if (page > 0) {
            menu.sendPage(page, player);
            return null;
        }

        menu.sendUsageError(arg, player);
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

    public static String doSpawn(MPlayer player) {
        CityWorld world = player.getCityWorld();
        Location spawn = world.getSpawnLocation();

        if (spawn == null) {
            //No spawn for the city world, default to world spawn
            spawn = world.getWorld().getSpawnLocation();
        }

        TPCD.makeCountdown(Mafiacraft.getPlugin(), 10, Type.CSPAWN, player.
                getBukkitEntity(), spawn);
        return null;
    }

}
