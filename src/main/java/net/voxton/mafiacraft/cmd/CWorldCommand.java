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
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.CityWorld;
import net.voxton.mafiacraft.geo.WorldToggle;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.voxton.mafiacraft.help.MenuType;
import net.voxton.mafiacraft.locale.Locale;
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
        MenuType.CWORLD.doHelp(player, arg);
        return null;
    }

    public static String doToggle(MPlayer player, String toggle) {
        if (!player.hasPermission("mafiacraft.admin")) {
            return player.getLocale().localize("command.general.not-allowed");
        }

        CityWorld world = player.getCityWorld();

        WorldToggle tog = null;
        try {
            tog = WorldToggle.valueOf(toggle.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return player.getLocale().localize("command.cworld.toggle-invalid",
                    Arrays.asList(WorldToggle.values()));
        }

        boolean val = world.toggle(tog);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.cworld.toggle-set", tog, val));
        return null;
    }

    public static String doSpawn(MPlayer player) {
        Location spawn = player.getCityWorld().getSpawnLocation();
        TPCD.makeCountdown(Mafiacraft.getPlugin(), 10, Type.CSPAWN, player.
                getBukkitEntity(), spawn);
        return null;
    }

}
