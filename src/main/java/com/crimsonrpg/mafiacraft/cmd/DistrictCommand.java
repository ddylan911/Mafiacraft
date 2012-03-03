/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.geo.City;
import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.geo.DistrictType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import com.crimsonrpg.mafiacraft.util.ValidationUtils;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class DistrictCommand {
    public static void parseCmd(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR + "Sorry, this command is only usable in game.");
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
            } else {
                result = doHelp(player);
            }
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("zone")) {
                result = doZone(player, largs.get(0));
            } else {
                result = doHelp(player);
            }
        } else {
            if (function.equalsIgnoreCase("desc")) {
                String desc = Joiner.on(' ').join(largs);
                result = doDesc(player, desc);
            } else {
                result = doHelp(player);
            }
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doDesc(MPlayer player, String description) {
        if (!player.hasPermission("mafiacraft.mod")) {
            return "You aren't allowed to use this command.";
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return "You aren't in a district that is part of a city.";
        }

        if (city.isMember(player)) {
            return "You must be a member of this city.";
        }

        description = description.trim();
        String result = ValidationUtils.validateDescription(description);
        if (result != null) {
            return result;
        }

        //Set the description
        district.setDescription(description);

        //Success!
        player.sendMessage(MsgColor.SUCCESS + "You have successfully changed the description of the district to:");
        player.sendMessage(MsgColor.INFO + description);
        return null;
    }

    public static String doHelp(MPlayer player) {
        if (!player.hasPermission("mafiacraft.visitor")) {
            return "You aren't allowed to use this command.";
        }

        //TODO: be helpful
        player.sendMessage(MsgColor.ERROR + "TODO: help");
        return null;
    }

    public static String doInfo(MPlayer player) {
        if (!player.hasPermission("mafiacraft.visitor")) {
            return "You aren't allowed to use this command.";
        }

        //TODO add real info
        player.sendMessage("You are in the district " + player.getDistrict().getNameInChat());
        return null;
    }

    public static String doSetBus(MPlayer player) {
        if (!player.hasPermission("mafiacraft.mod")) {
            return "You aren't allowed to use this command.";
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return "You aren't in a district that is part of a city.";
        }

        if (city.isMember(player)) {
            return "You must be a member of this city.";
        }

        Location bus = player.getLocation();
        district.setBusStop(bus);

        player.sendMessage(MsgColor.SUCCESS + "The bus stop of " + district.getNameInChat() + " has been set to your location.");
        return null;
    }

    public static String doZone(MPlayer player, String typeString) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL + "http://voxton.net/" + ".";
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
        player.sendMessage(MsgColor.SUCCESS + "The district has been zoned to a " + type.niceName() + " district successfully.");
        return null;
    }

}
