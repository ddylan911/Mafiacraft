/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.geo.City;
import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import org.bukkit.Chunk;

/**
 *
 * @author simplyianm
 */
public class CityCommand {
    public static String doFound(MPlayer player, String name) {
        double balance = player.getMoney();
        double required = Mafiacraft.getInstance().getConfig().getDouble("prices.cityfound", 1000000.0);

        if (balance < required) {
            return "You don't have enough money to found a city! (Costs $1,000,000)";
        }

        if (!name.matches("[\\w\\s]+") || name.length() > 25) {
            return "That city name is invalid; city names must be less than 25 characters in length and alphanumeric.";
        }

        if (Mafiacraft.getInstance().getCityManager().cityExists(name)) {
            return "A city with that name already exists; please try another name.";
        }

        //Found a city
        Chunk sample = player.getPlayer().getLocation().getChunk();
        District district = Mafiacraft.getInstance().getCityManager().createDistrict(sample);
        City city = Mafiacraft.getInstance().getCityManager().foundCity(name, district);

        //Notify
        player.getPlayer().sendMessage(MsgColor.SUCCESS + "Your city has been founded successfully.");
        return null;
    }

}
