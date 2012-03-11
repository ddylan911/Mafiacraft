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

import net.voxton.mafiacraft.MConfig;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.City;
import net.voxton.mafiacraft.geo.CityWorld;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.util.TPCD;
import net.voxton.mafiacraft.util.ValidationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.voxton.mafiacraft.help.MenuType;
import net.voxton.mafiacraft.locale.Locale;
import net.voxton.mafiacraft.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Contains all city-related commands.
 */
public final class CityCommand {

    public static void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    MsgColor.ERROR
                    + Locale.getDefault().localize("command.general.ingame-only"));
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
            if (function.equalsIgnoreCase("annex")) {
                result = doAnnex(player);
            } else if (function.equalsIgnoreCase("funds")) {
                result = doFunds(player);
            } else if (function.equalsIgnoreCase("spawn")) {
                result = doSpawn(player);
            } else if (function.equalsIgnoreCase("setspawn")) {
                result = doSetSpawn(player);
            } else if (function.equalsIgnoreCase("unannex")) {
                result = doUnannex(player);
            } else if (function.equalsIgnoreCase("disband")) {
                result = doDisband(player);
            } else if (function.equalsIgnoreCase("claim")) {
                result = doClaim(player);
            } else if (function.equalsIgnoreCase("unclaim")) {
                result = doUnclaim(player);
            } else {
                result = doHelp(player, function);
            }
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("bus")) {
                result = doBus(player, largs.get(0));
            } else if (function.equalsIgnoreCase("found")) {
                result = doFound(player, largs.get(0));
            } else if (function.equalsIgnoreCase("rename")) {
                result = doRename(player, largs.get(0));
            } else if (function.equalsIgnoreCase("deposit")) {
                result = doDeposit(player, largs.get(0));
            } else if (function.equalsIgnoreCase("withdraw")) {
                result = doWithdraw(player, largs.get(0));
            } else if (function.equalsIgnoreCase("setchief")) {
                result = doSetChief(player, largs.get(0));
            } else if (function.equalsIgnoreCase("setassistant")) {
                result = doSetAssistant(player, largs.get(0));
            } else {
                result = doHelp(player, function);
            }
        } else if (largs.size() < 3) {
            if (function.equalsIgnoreCase("makepolice")) {
                result = doMakePolice(player, largs.get(0), largs.get(1));
            } else {
                result = doHelp(player, function);
            }
        } else {
            result = doHelp(player, function);
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doHelp(MPlayer player) {
        MenuType.CITY.getPage(1);
        return null;
    }

    public static String doHelp(MPlayer player, String arg) {
        MenuType.CITY.doHelp(player, arg);
        return null;
    }

    public static String doFound(MPlayer player, String name) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.citizen-only");
        }

        CityWorld cw = player.getCityWorld();
        City capital = cw.getCapital();
        if (capital != null) {
            return player.getLocale().localize(
                    "command.city.capital-established");
        }

        double balance = player.getMoney();
        double foundCost = MConfig.getDouble("prices.city.found");

        if (balance < foundCost) {
            return player.getLocale().localize("command.city.no-money.found",
                    StringUtils.formatCurrency(foundCost));
        }

        if (!name.matches("[\\w\\s]+") || name.length() > 25) {
            return player.getLocale().localize("command.city.invalid-name");
        }

        if (Mafiacraft.getCityManager().cityExists(name)) {
            return player.getLocale().localize("command.city.name-taken");
        }

        //Found a city
        Chunk sample = player.getBukkitEntity().getLocation().getChunk();
        District district = Mafiacraft.getDistrict(sample);
        City city =
                Mafiacraft.getCityManager().foundCity(player, name, district);
        player.transferMoney(city, foundCost);

        //Notify
        player.getBukkitEntity().sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.city.founded"));
        return null;
    }

    public static String doSetSpawn(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMember(player)) {
            return player.getLocale().localize("command.city.not-allowed");
        }

        city.setSpawnLocation(player.getLocation());
        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.city.set-spawn"));
        return null;
    }

    public static String doSpawn(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        Location spawn = city.getSpawnLocation();
        if (spawn == null) {
            return player.getLocale().localize("command.city.no-spawn");
        }

        int citySpawnTime = MConfig.getInt("warmup.cityspawn");
        TPCD.makeCountdown(Mafiacraft.getPlugin(), citySpawnTime,
                TPCD.Type.CSPAWN, player.getBukkitEntity(), spawn);
        return null;
    }

    public static String doAnnex(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCityWorld().getCapital();
        if (city == null) {
            return player.getLocale().localize("command.city.none-in-world");
        }

        District district = player.getDistrict();
        if (district.getCity() != null) {
            return player.getLocale().localize(
                    "command.city.district-unavailable");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.district.must-be-mayor.annex");
        }

        double cost = MConfig.getDouble("prices.city.annex");
        if (!city.hasEnough(cost)) {
            return player.getLocale().localize("command.city.no-money.annex",
                    StringUtils.formatCurrency(cost));
        }

        city.subtractMoney(cost);
        city.attachNewDistrict(district);
        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.district.claimed"));
        return null;
    }

    public static String doUnannex(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.general.not-in-city");
        }

        District district = player.getDistrict();
        if (district.getCity() == null) {
            return player.getLocale().localize("command.district.not-associated");
        }

        if (!district.getCity().equals(city)) {
            return player.getLocale().localize("command.district.not-owned");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.district.must-be-mayor.unannex");
        }

        district.detachFromCity();
        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.district.unclaimed"));
        return null;
    }

    public static String doRename(MPlayer player, String name) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-mayor.rename");
        }

        String valid = ValidationUtils.validateName(name);
        if (valid != null) {
            return valid;
        }

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.renamed", city.getName(), name));
        city.setName(name);
        return null;
    }

    public static String doFunds(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMember(player)) {
            return player.getLocale().localize(
                    "command.city.must-be.member.funds");
        }

        double funds = city.getMoney();
        String fundsStr = StringUtils.formatCurrency(funds);
        player.sendMessage(MsgColor.INFO + player.getLocale().localize(
                "command.city.funds", city.getOwnerName(), fundsStr));
        return null;
    }

    public static String doDisband(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("commmand.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-mayor.disband");
        }

        city.disband(); //Holy crap!
        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.city.disbanded"));
        return null;
    }

    public static String doBus(MPlayer player, String districtName) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        int busDist = MConfig.getInt("district.bus-max-distance");

        District thisDist = player.getDistrict();
        Location thisBus = thisDist.getBusStop();
        if (player.getLocation().distanceSquared(thisBus) > busDist * busDist) {
            return player.getLocale().localize(
                    "command.district.bus-max-distance", busDist);
        }

        districtName = districtName.toUpperCase().trim();
        District d = city.getDistrictByName(districtName);
        if (d == null) {
            return player.getLocale().localize("command.district.not-exist",
                    districtName);
        }

        Location bus = d.getBusStop();
        if (bus == null) {
            return player.getLocale().localize("command.district.no-bus");
        }

        TPCD.makeCountdown(Mafiacraft.getPlugin(), 10, TPCD.Type.DBUS, player.
                getBukkitEntity(), bus);
        return null;
    }

    public static String doDeposit(MPlayer player, String amount) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        double amt;
        try {
            amt = Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            return player.getLocale().localize("command.general.invalid-number",
                    amount);
        }

        String amtFmt = StringUtils.formatCurrency(amt);
        if (!player.hasEnough(amt)) {
            return player.getLocale().localize(
                    "command.general.no-money.deposit", amtFmt, StringUtils.
                    formatCurrency(player.getMoney()));
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        player.transferMoney(city, amt);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.deposited", amtFmt, city.getOwnerName()));
        return null;
    }

    public static String doWithdraw(MPlayer player, String amount) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.citizen");
        }

        double amt;
        try {
            amt = Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            return player.getLocale().localize("command.general.invalid-number");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-mayor.withdraw");
        }

        String amtFmt = StringUtils.formatCurrency(amt);
        if (!city.hasEnough(amt)) {
            return player.getLocale().localize("command.city.no-money.withdraw",
                    StringUtils.formatCurrency(city.getMoney()), amtFmt);
        }

        city.transferMoney(player, amt);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "comand.city.withdraw", amtFmt, city.getOwnerName()));
        return null;
    }

    public static String doClaim(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMember(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-member.claim");
        }

        District d = player.getDistrict();
        if (d.getType().isGovernment()) {
            return player.getLocale().localize("command.city.already-owned");
        }

        double amt = MConfig.getDouble("prices.city.claim");
        String amtFmt = StringUtils.formatCurrency(amt);
        if (!city.hasEnough(amt)) {
            return player.getLocale().localize("command.city.no-money.claim",
                    amtFmt);
        }

        Chunk chunk = player.getChunk();
        String sn = city.getSectionName(chunk);

        //Claim the section
        d.setOwner(chunk, city);

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.claimed", sn));
        return null;
    }

    public static String doUnclaim(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCity();
        if (city == null) {
            return "You are not in a city.";
        }

        if (!city.isMember(player)) {
            return "You must be a member of the city to perform this action.";
        }

        District d = player.getDistrict();
        if (d.getType().isGovernment()) {
            return "This district is already a government district.";
        }

        Chunk chunk = player.getChunk();
        String sn = city.getSectionName(chunk);

        //Unclaim the section
        d.removeOwner(chunk);

        player.sendMessage(MsgColor.SUCCESS + "You have unclaimed the section "
                + sn + " for your city.");
        return null;
    }

    public static String doMakePolice(MPlayer player, String chief,
            String assistant) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCity();
        if (city == null) {
            return "You are not in a city.";
        }

        if (!city.isMayor(player)) {
            return "You must be a member of the city to perform this action.";
        }

        MPlayer c = Mafiacraft.getPlayer(Bukkit.getPlayer(chief));
        if (c == null) {
            return "That player is either offline or does not exist.";
        }

        MPlayer a = Mafiacraft.getPlayer(Bukkit.getPlayer(assistant));
        if (a == null) {
            return "That player is either offline or does not exist.";
        }

        double amt = MConfig.getDouble("prices.city.makepolice");
        if (!city.hasEnough(amt)) {
            return "The city does not have enough money to establish police. (Costs "
                    + amt + ")";
        }

        city.establishPolice(c, a);
        player.sendMessage(MsgColor.SUCCESS
                + "A police force has been established in your city.");
        return null;
    }

    public static String doSetChief(MPlayer player, String chief) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCity();
        if (city == null) {
            return "You are not in a city.";
        }

        if (!city.isMayor(player)) {
            return "You must be a member of the city to perform this action.";
        }

        MPlayer c = Mafiacraft.getPlayer(Bukkit.getPlayer(chief));
        if (c == null) {
            return "That player is either offline or does not exist.";
        }

        Government police = city.getPolice();
        if (police == null) {
            return "The city does not have a police force established.";
        }

        police.setLeader(c);
        player.sendMessage(MsgColor.SUCCESS
                + "The chief of police of the city has successfully been changed to "
                + c.getName() + ".");
        return null;
    }

    public static String doSetAssistant(MPlayer player, String assistant) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCity();
        if (city == null) {
            return "You are not in a city.";
        }

        if (!city.isMayor(player)) {
            return "You must be a member of the city to perform this action.";
        }

        MPlayer a = Mafiacraft.getPlayer(Bukkit.getPlayer(assistant));
        if (a == null) {
            return "That player is either offline or does not exist.";
        }

        Government police = city.getPolice();
        if (police == null) {
            return "The city does not have a police force established.";
        }

        police.setViceLeader(a);
        player.sendMessage(MsgColor.SUCCESS
                + "The assistant chief of the city has successfully been changed to "
                + a.getName() + ".");
        return null;
    }

}
