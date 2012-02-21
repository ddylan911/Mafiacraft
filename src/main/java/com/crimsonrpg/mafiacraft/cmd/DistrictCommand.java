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

        if (largs.size() < 0) {
        }
    }

    public static String doHelp(MPlayer player) {
        //TODO: be helpful
        player.sendMessage(MsgColor.ERROR + "TODO: help");
        return null;
    }

    public static String doSetBus(MPlayer player) {
        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return "You aren't in a district that is part of a city.";
        }

        if (city.isMember(player)) {
            return "You must be a mayor of this city.";
        }

        Location bus = player.getLocation();
        district.setBusStop(bus);

        player.sendMessage(MsgColor.SUCCESS + "The bus stop of " + district.getNameInChat() + " has been set to your location.");
        return null;
    }

    public static String doZone(MPlayer player, String typeString) {
        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return "You aren't in a district that is part of a city.";
        }

        City oc = player.getOwnedCity();
        if (oc == null || city.equals(oc)) {
            return "You must be a mayor of this city.";
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
