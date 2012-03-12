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
import net.voxton.mafiacraft.geo.City;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.geo.DistrictType;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.util.ValidationUtils;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.voxton.mafiacraft.help.MenuType;
import net.voxton.mafiacraft.util.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commands relating to districts.
 */
public final class DistrictCommand {

    public static void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR + Mafiacraft.getDefaultLocale().
                    localize("command.general.ingame-only"));
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
            if (function.equalsIgnoreCase("setbus")) {
                result = doSetBus(player);
            } else if (function.equalsIgnoreCase("info")) {
                result = doInfo(player);
            } else if (function.equalsIgnoreCase("claimgrid")) {
                result = doClaimGrid(player);
            } else {
                result = doHelp(player, function);
            }
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("zone")) {
                result = doZone(player, largs.get(0));
            } else if (function.equalsIgnoreCase("setcost")) {
                result = doSetCost(player, largs.get(0));
            } else if (function.equalsIgnoreCase("help")) {
                result = doHelp(player, largs.get(0));
            } else {
                result = doHelp(player, function);
            }
        } else {
            if (function.equalsIgnoreCase("desc")) {
                String desc = Joiner.on(' ').join(largs);
                result = doDesc(player, desc);
            } else {
                result = doHelp(player, function);
            }
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doDesc(MPlayer player, String description) {
        if (!player.hasPermission("mafiacraft.mod")) {
            return player.getLocale().localize("command.general.not-allowed");
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return player.getLocale().localize(
                    "command.district.this-not-associated");
        }

        if (city.isMember(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-member.district.desc");
        }

        description = description.trim();
        String result = ValidationUtils.validateDescription(description);
        if (result != null) {
            return result;
        }

        //Set the description
        district.setDescription(description);

        //Success!
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.district.desc", description));
        return null;
    }

    public static String doHelp(MPlayer player) {
        MenuType.DISTRICT.doHelp(player);
        return null;
    }

    public static String doHelp(MPlayer player, String arg) {
        MenuType.DISTRICT.doHelp(player, arg);
        return null;
    }

    public static String doInfo(MPlayer player) {
        //TODO add real info
        player.sendMessage("You are in the district " + player.getDistrict().
                getNameInChat());
        return null;
    }

    public static String doSetBus(MPlayer player) {
        if (!player.hasPermission("mafiacraft.mod")) {
            return player.getLocale().localize("command.general.not-allowed");
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return player.getLocale().localize(
                    "command.district.this-not-associated");
        }

        if (city.isMember(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-member.district.setbus");
        }

        Location bus = player.getLocation();
        district.setBusStop(bus);

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.district.bus-set", district.getNameInChat()));
        return null;
    }

    public static String doZone(MPlayer player, String typeString) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return "You aren't in a district that is part of a city.";
        }

        if (!city.isMayor(player)) {
            return "You aren't the mayor of this city.";
        }

        DistrictType type = DistrictType.fromString(typeString);
        if (type == null) {
            return "There is no district type with the name specified.";
        }

        if (type.equals(DistrictType.UNEXPLORED)) {
            return "You are not allowed to unexplore districts. That would not make much sense, would it?";
        }

        district.resetOwnerships().setType(type);
        player.sendMessage(MsgColor.SUCCESS
                + "The district has been zoned to a " + type.niceName()
                + " district successfully.");
        return null;
    }

    public static String doClaimGrid(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return "You aren't in a district that is part of a city.";
        }

        if (!city.isMayor(player)) {
            return "You aren't the mayor of this city.";
        }

        boolean check = city.claimGridAndCheck(district);
        if (!check) {
            return "You can't claim this.";
        }

        player.sendMessage(MsgColor.SUCCESS
                + "The grid for the city has been claimed.");
        return null;
    }

    public static String doSetCost(MPlayer player, String amount) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return "You aren't in a district that is part of a city.";
        }

        if (!city.isMayor(player)) {
            return "You aren't the mayor of this city.";
        }

        double cost = 0;
        try {
            cost = Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            return "Invalid number '" + amount + "'.";
        }

        district.setLandCost(cost);

        player.sendMessage(MsgColor.SUCCESS
                + "The land cost of the district has been set to $"
                + StringUtils.formatCurrency(cost)
                + ".");
        return null;
    }

}
