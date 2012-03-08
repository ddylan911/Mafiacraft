/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.MConfig;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.MafiacraftPlugin;
import net.voxton.mafiacraft.geo.City;
import net.voxton.mafiacraft.geo.CityWorld;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.util.TPCD;
import net.voxton.mafiacraft.util.ValidationUtils;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.voxton.mafiacraft.help.MenuType;
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
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        CityWorld cw = player.getCityWorld();
        City capital = cw.getCapital();
        if (capital != null) {
            return "The world you are in already has a capital established.";
        }

        double balance = player.getMoney();
        double foundCost = MConfig.getDouble("prices.city.found");

        if (balance < foundCost) {
            return "You don't have enough money to found a city! (Costs $1,000,000)";
        }

        if (!name.matches("[\\w\\s]+") || name.length() > 25) {
            return "That city name is invalid; city names must be less than 25 characters in length and alphanumeric.";
        }

        if (Mafiacraft.getCityManager().cityExists(name)) {
            return "A city with that name already exists; please try another name.";
        }

        //Found a city
        Chunk sample = player.getBukkitEntity().getLocation().getChunk();
        District district = Mafiacraft.getDistrict(sample);
        City city =
                Mafiacraft.getCityManager().foundCity(player, name, district);
        player.transferMoney(city, foundCost);

        //Notify
        player.getBukkitEntity().sendMessage(MsgColor.SUCCESS
                + "Your city has been founded successfully.");
        return null;
    }

    public static String doSetSpawn(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        if (!city.isMember(player)) {
            return "You aren't allowed to do this in this city.";
        }

        city.setSpawnLocation(player.getLocation());
        player.sendMessage(MsgColor.SUCCESS
                + "You have set your city's spawn location successfully.");
        return null;
    }

    public static String doSpawn(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        Location spawn = city.getSpawnLocation();
        if (spawn == null) {
            return "The city does not have a spawn location set.";
        }

        int citySpawnTime = MConfig.getInt("warmup.cityspawn");
        TPCD.makeCountdown(Mafiacraft.getPlugin(), citySpawnTime,
                TPCD.Type.CSPAWN, player.getBukkitEntity(), spawn);
        return null;
    }

    public static String doAnnex(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCityWorld().getCapital();
        if (city == null) {
            return "There is no city established in this world.";
        }

        District district = player.getDistrict();
        if (district.getCity() != null) {
            return "This district is already associated with the city.";
        }

        if (!city.isMayor(player)) {
            return "You must be the mayor of the city to annex new districts.";
        }

        double cost = MConfig.getDouble("prices.city.annex");
        if (!city.hasEnough(cost)) {
            return "The city doesn't have enough money to do claim this district.";
        }

        city.subtractMoney(cost);
        city.attachNewDistrict(district);
        player.sendMessage(MsgColor.SUCCESS
                + "You have successfuly claimed the district for your city.");
        return null;
    }

    public static String doUnannex(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

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
        player.sendMessage(MsgColor.SUCCESS
                + "You have successfully unclaimed the district from your city.");
        return null;
    }

    public static String doRename(MPlayer player, String name) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

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

        player.sendMessage(MsgColor.SUCCESS + city.getName()
                + " has been renamed to " + name + ".");
        city.setName(name);
        return null;
    }

    public static String doFunds(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        if (!city.isMember(player)) {
            return "You must be part of the city government to view the funds of the city.";
        }

        double funds = city.getMoney();
        String fundsStr = NumberFormat.getCurrencyInstance(Locale.ENGLISH).
                format(funds);
        player.sendMessage(MsgColor.INFO + city.getOwnerName() + " has "
                + fundsStr + " in funds.");
        return null;
    }

    public static String doDisband(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        if (!city.isMayor(player)) {
            return "Only the mayor can disband the city. This is very dangerous.";
        }

        city.disband(); //Holy crap!
        player.sendMessage(MsgColor.SUCCESS
                + "The city has been disbanded. Anarchy will likely take place.");
        return null;
    }

    public static String doBus(MPlayer player, String districtName) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        District thisDist = player.getDistrict();
        if (thisDist == null) {
            return "District not found error.";
        }

        Location thisBus = thisDist.getBusStop();
        if (player.getLocation().distanceSquared(thisBus) > 100) {
            return "You must be within 10 blocks of a bus stop to ride the bus.";
        }

        District d = city.getDistrictByName(districtName);
        if (d == null) {
            return "That district does not exist.";
        }

        Location bus = d.getBusStop();
        if (bus == null) {
            return "That district does not have a bus stop.";
        }

        TPCD.makeCountdown(Mafiacraft.getPlugin(), 10, TPCD.Type.DBUS, player.
                getBukkitEntity(), bus);
        return null;
    }

    public static String doDeposit(MPlayer player, String amount) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        double amt;
        try {
            amt = Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            return "The amount you specified is an invalid number.";
        }

        if (!player.hasEnough(amt)) {
            return "You don't have that much money to burn.";
        }

        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        player.transferMoney(city, amt);
        String fmt =
                NumberFormat.getCurrencyInstance(Locale.ENGLISH).format(amt);
        player.sendMessage(MsgColor.SUCCESS + "You have deposited " + fmt
                + " into " + city.getOwnerName() + ".");
        return null;
    }

    public static String doWithdraw(MPlayer player, String amount) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        double amt;
        try {
            amt = Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            return "The amount you specified is an invalid number.";
        }

        City city = player.getCity();
        if (city == null) {
            return "You aren't in a city.";
        }

        if (!city.isMayor(player)) {
            return "You must be mayor or above to perform this action.";
        }

        if (!city.hasEnough(amt)) {
            return "The city doesn't have that much money.";
        }

        city.transferMoney(player, amt);
        String fmt =
                NumberFormat.getCurrencyInstance(Locale.ENGLISH).format(amt);
        player.sendMessage(MsgColor.SUCCESS + "You have deposited $" + fmt
                + " into " + city.getOwnerName() + ".");
        return null;
    }

    public static String doClaim(MPlayer player) {
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

        double amt = MConfig.getDouble("prices.city.claim");
        String af = NumberFormat.getCurrencyInstance(Locale.ENGLISH).format(amt);
        if (!city.hasEnough(amt)) {
            return "The city doesn't have enough money to claim land. (Costs "
                    + af + ")";
        }

        Chunk chunk = player.getChunk();
        String sn = city.getSectionName(chunk);

        //Claim the section
        d.setOwner(chunk, city);

        player.sendMessage(MsgColor.SUCCESS + "You have claimed the section "
                + sn + " for your city.");
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
