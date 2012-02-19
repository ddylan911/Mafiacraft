/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.MConfig;
import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.geo.City;
import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import com.crimsonrpg.mafiacraft.util.TPCD;
import com.crimsonrpg.mafiacraft.util.ValidationUtils;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Contains all city-related commands.
 */
public class CityCommand {
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
            if (function.equalsIgnoreCase("claim")) {
                result = doClaim(player);
            } else if (function.equalsIgnoreCase("funds")) {
                result = doFunds(player);
            } else if (function.equalsIgnoreCase("spawn")) {
                result = doSpawn(player);
            } else if (function.equalsIgnoreCase("setspawn")) {
                result = doSetSpawn(player);
            } else if (function.equalsIgnoreCase("unclaim")) {
                result = doUnclaim(player);
            } else {
                result = doHelp(player);
            }
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("bus")) {
                result = doBus(player, largs.get(0));
            } else if (function.equalsIgnoreCase("found")) {
                result = doFound(player, largs.get(0));
            } else if (function.equalsIgnoreCase("rename")) {
                result = doRename(player, largs.get(0));
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

    public static String doFound(MPlayer player, String name) {
        City ct = player.getCity();
        if (ct != null) {
            return "You are already in a city.";
        }

        double balance = player.getMoney();
        double required = MConfig.getDouble("prices.city.found");

        if (balance < required) {
            return "You don't have enough money to found a city! (Costs $1,000,000)";
        }

        if (!name.matches("[\\w\\s]+") || name.length() > 25) {
            return "That city name is invalid; city names must be less than 25 characters in length and alphanumeric.";
        }

        if (MafiacraftPlugin.getInstance().getCityManager().cityExists(name)) {
            return "A city with that name already exists; please try another name.";
        }

        //Found a city
        Chunk sample = player.getBukkitEntity().getLocation().getChunk();
        District district = Mafiacraft.getDistrict(sample);
        City city = Mafiacraft.getCityManager().foundCity(name, district);

        //Notify
        player.getBukkitEntity().sendMessage(MsgColor.SUCCESS + "Your city has been founded successfully.");
        return null;
    }

    public static String doHelp(MPlayer player) {
        player.sendMessage("TODO:help");
        //TODO: help
        return null;
    }

    public static String doSetSpawn(MPlayer player) {
        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        if (!city.isMember(player)) {
            return "You aren't allowed to do this in this city.";
        }

        city.setSpawnLocation(player.getLocation());
        player.sendMessage(MsgColor.SUCCESS + "You have set your city's spawn location successfully.");
        return null;
    }

    public static String doSpawn(MPlayer player) {
        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        int citySpawnTime = MConfig.getInt("warmup.cityspawn");
        TPCD.makeCountdown(MafiacraftPlugin.getInstance(), citySpawnTime, TPCD.Type.CSPAWN, player.getBukkitEntity(), city.getSpawnLocation());
        return null;
    }

    public static String doClaim(MPlayer player) {
        City city = player.getOwnedCity();
        if (city == null) {
            return "You aren't a mayor of any city.";
        }

        District district = player.getDistrict();
        if (district.getCity() != null) {
            return "This district is already associated with a city.";
        }

        if (!city.isMayor(player)) {
            return "You aren't allowed to do this in the city.";
        }

        double cost = MConfig.getDouble("prices.city.claim");
        if (!city.hasEnough(cost)) {
            return "You don't have enough money to do claim this district for the city.";
        }

        city.subtractMoney(cost);
        city.attachNewDistrict(district);
        player.sendMessage(MsgColor.SUCCESS + "You have successfuly claimed the district for your city.");
        return null;
    }

    public static String doUnclaim(MPlayer player) {
        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        District district = player.getDistrict();
        if (district.getCity() == null) {
            return "This district is not associated with a city.";
        }

        if (!district.getCity().equals(city)) {
            return "This district is not part of your city.";
        }

        if (!city.isMayor(player)) {
            return "You aren't allowed to do this in the city.";
        }

        district.detachFromCity();
        player.sendMessage(MsgColor.SUCCESS + "You have successfully unclaimed the district from your city.");
        return null;
    }

    public static String doRename(MPlayer player, String name) {
        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        if (!city.isMayor(player)) {
            return "You must be the mayor of this city to rename it.";
        }

        String valid = ValidationUtils.validateName(name);
        if (valid != null) {
            return valid;
        }

        player.sendMessage(MsgColor.SUCCESS + city.getName() + " has been renamed to " + name + ".");
        city.setName(name);
        return null;
    }

    public static String doFunds(MPlayer player) {
        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        if (!city.isMember(player)) {
            return "You must be part of the city government to view the funds of the city.";
        }

        double funds = city.getMoney();
        String fundsStr = NumberFormat.getCurrencyInstance(Locale.ENGLISH).format(funds);
        player.sendMessage(MsgColor.INFO + city.getOwnerName() + " has " + fundsStr + " in funds.");
        return null;
    }

    public static String doDisband(MPlayer player) {
        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        if (!city.isMayor(player)) {
            return "Only the mayor can disband the city. This is very dangerous.";
        }

        city.disband(); //Holy crap!
        player.sendMessage(MsgColor.SUCCESS + "The city has been disbanded. Anarchy will likely take place.");
        return null;
    }

    public static String doBus(MPlayer player, String districtName) {
        //TODO: check permissions.

        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        District d = city.getDistrictByName(districtName);
        if (d == null) {
            return "That district does not exist.";
        }

        Location bus = d.getBusStop();
        if (bus == null) {
            return "That district does not have a bus stop.";
        }

        TPCD.makeCountdown(MafiacraftPlugin.getInstance(), 10, TPCD.Type.DBUS, player.getBukkitEntity(), bus);
        return null;
    }

}
