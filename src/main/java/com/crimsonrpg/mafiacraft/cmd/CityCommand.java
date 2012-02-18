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
import org.bukkit.Chunk;

/**
 * Contains all city-related commands.
 */
public class CityCommand {
    public static String doFound(MPlayer player, String name) {
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

    public static String doSetSpawn(MPlayer player) {
        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }
        return null;
    }

    public static String doClaim(MPlayer player) {
        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        District district = player.getDistrict();
        if (district.getCity() != null) {
            return "That district is already associated with a city.";
        }

        if (!city.isMember(player)) {
            return "You aren't allowed to do this in the city.";
        }

        if (!city.hasEnough(MConfig.getDouble("prices.city.claim"))) {
            return "You don't have enough money to do claim this land for the city.";
        }

        district.setCity(city);
        player.sendMessage(MsgColor.SUCCESS + "You have successfuly claimed the district for your city.");
        return null;
    }

}
