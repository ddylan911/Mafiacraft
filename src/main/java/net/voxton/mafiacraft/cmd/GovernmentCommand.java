/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.MConfig;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.MafiacraftPlugin;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.gov.Division;
import net.voxton.mafiacraft.gov.GovType;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.gov.Position;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.util.TPCD;
import net.voxton.mafiacraft.util.ValidationUtils;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Holds government-related commands.
 */
public final class GovernmentCommand {

    public static void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args, GovType type) {
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
            if (function.equalsIgnoreCase("who")) {
                result = doWho(player);
            } else if (function.equalsIgnoreCase("claim")) {
                result = doClaim(player);
            } else if (function.equalsIgnoreCase("hq")) {
                result = doHq(player);
            } else if (function.equalsIgnoreCase("sethq")) {
                result = doSetHq(player);
            } else if (function.equalsIgnoreCase("leave")) {
                result = doLeave(player, type);
            } else if (function.equalsIgnoreCase("player")) {
                result = doPlayer(player);
            } else if (function.equalsIgnoreCase("accept")) {
                result = doAccept(player, type);
            } else {
                result = doHelp(player);
            }
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("who")) {
                result = doWho(player, largs.get(0));
            } else if (function.equalsIgnoreCase("invite")) {
                result = doInvite(player, largs.get(0));
            } else if (function.equalsIgnoreCase("createdivision")) {
                result = doCreateDivision(player, largs.get(0));
            } else if (function.equalsIgnoreCase("kick")) {
                result = doKick(player, largs.get(0));
            } else if (function.equalsIgnoreCase("player")) {
                result = doPlayer(player, largs.get(0));
            } else if (function.equalsIgnoreCase("found")) {
                result = doFound(player, largs.get(0), type);
            } else if (function.equalsIgnoreCase("promoteofficer")) {
                result = doPromoteOfficer(player, largs.get(0));
            } else if (function.equalsIgnoreCase("demoteofficer")) {
                result = doDemoteOfficer(player, largs.get(0));
            } else {
                result = doHelp(player);
            }
        } else if (largs.size() < 3) {
            if (function.equalsIgnoreCase("grant")) {
                result = doGrant(player, largs.get(0), largs.get(1));
            } else if (function.equalsIgnoreCase("setmanager")
                    || function.equalsIgnoreCase("setcapo")
                    || function.equalsIgnoreCase("setsergeant")) {
                result = doSetManager(player, largs.get(0), largs.get(1));
            } else {
                result = doHelp(player);
            }
        } else {
            if (function.equalsIgnoreCase("found")) {
                result = doFound(player, Joiner.on(' ').join(largs), type);
            } else {
                result = doHelp(player);
            }
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doHelp(MPlayer player) {
        if (!player.hasPermission("mafiacraft.visitor")) {
            return "You aren't allowed to use this command.";
        }

        player.sendMessage("TODO: help");
        //TODO: help
        return null;
    }

    public static String doAccept(MPlayer player, GovType type) {
        Integer inv = player.getSessionStore().getInt("gov-inv", -1);
        if (inv < 0) {
            return "You have not been invited to a " + type.getName() + ".";
        }

        Government gov = Mafiacraft.getGovernmentManager().getGovernment(inv);
        if (gov == null) {
            return "The " + type.getName()
                    + " you were invited to no longer exists.";
        }

        gov.addAffiliate(player);

        player.sendMessage(MsgColor.SUCCESS + "You have joined the " + type.
                getName() + " " + gov.getName() + ".");
        gov.broadcastMessage(player.getName() + " has joined the "
                + gov.getType().
                getName() + ".");
        return null;
    }

    public static String doFound(MPlayer player, String name, GovType type) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        if (type.equals(GovType.POLICE)) {
            return "You can't found a police.";
        }

        if (!type.canFound()) {
            return "You can't found a " + type.getName() + ".";
        }

        double balance = player.getMoney();
        double cost = MConfig.getDouble("mafia.found");

        if (balance < cost) {
            return "You don't have enough money to do this. (Costs $" + cost
                    + ")";
        }

        if (player.getGovernment() != null) {
            return "You are already in a government!";
        }

        name = name.trim();
        String result = ValidationUtils.validateName(name);
        if (result != null) {
            return result;
        }

        if (Mafiacraft.getGovernmentManager().getGovernment(name) != null) {
            return "Another government with that name already exists.";
        }

        //Found the government
        Government founded = Mafiacraft.getGovernmentManager().createGovernment(
                name, type);
        if (!founded.addAffiliate(player)) {
            return "Error adding. We can't do math.";
        }

        founded.setLeader(player);
        
        double startupCapital = MConfig.getDouble("mafia.startupcapital");
        founded.addMoney(startupCapital);
        
        player.sendMessage(MsgColor.SUCCESS
                + "You have successfully founded a new " + type.getName() + ".");
        return null;
    }

    public static String doPlayer(MPlayer player) {
        return doPlayer(player, player.getName());
    }

    public static String doPlayer(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        MPlayer tgt = Mafiacraft.getPlayer(Bukkit.getPlayer(target));
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        player.sendMessage(MsgColor.INFO + "TODO: finish info for " + tgt.
                getDisplayName());
        player.sendMessage(MsgColor.INFO + "Position: " + tgt.getPosition());
        //TODO: add stats
        return null;
    }

    public static String doWho(MPlayer player) {
        return doWho(player, player.getGovernment().getName());
    }

    public static String doWho(MPlayer player, String govName) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov =
                Mafiacraft.getGovernmentManager().getGovernment(govName);
        if (gov == null) {
            return "The government you specified does not exist.";
        }

        player.sendMessage(MsgColor.INFO + "=== Info for " + gov.getName()
                + " ===");
        //TODO: add stats
        return null;
    }

    public static String doHq(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();

        if (gov == null) {
            return "You are not part of a government.";
        }

        if (!player.getPosition().isAtLeast(Position.WORKER)) {
            return "Your position in your " + gov.getType().getName()
                    + " is not high enough to use HQ teleport.";
        }

        Location hq = gov.getHq();
        if (hq == null) {
            return "Your " + gov.getType().getName()
                    + " does not have a HQ set.";
        }

        //Teleport.
        TPCD.makeCountdown(Mafiacraft.getPlugin(), 10, TPCD.Type.GOVHQ, player.
                getBukkitEntity(), hq);
        return null;
    }

    public static String doInvite(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        MPlayer tgt = Mafiacraft.getPlayer(Bukkit.getPlayer(target));
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not part of a government.";
        }

        if (!player.getPosition().isAtLeast(Position.WORKER)) {
            System.out.println(player.getPosition());
            return "Your position in your " + gov.getType().getName()
                    + " is not high enough to invite people to your government.";
        }

        Government other = tgt.getGovernment();
        if (other != null) {
            if (other.equals(gov)) {
                return "The other player you specified is already in your "
                        + gov.getType().getName() + ".";
            }
            return "The player you specified is already affiliated with a government.";
        }

        gov.dispatchInvite(player, tgt);
        player.sendMessage(MsgColor.INFO
                + "An invite to the mafia has been dispatched to "
                + tgt.getName() + ".");
        return null;
    }

    public static String doKick(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        MPlayer tgt = Mafiacraft.getPlayer(Bukkit.getPlayer(target));
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not part of a government.";
        }

        if (!player.getPosition().isAtLeast(Position.WORKER)) {
            return "Your position in your " + gov.getType().getName()
                    + " is not high enough to kick people from your government.";
        }

        Government other = tgt.getGovernment();
        if (other == null) {
            return "That player is not in a " + gov.getType().getName() + ".";
        }

        if (!other.equals(gov)) {
            return "That player is not in your " + gov.getType().getName() + ".";
        }

        if (!tgt.getPosition().equals(Position.AFFILIATE)) {
            return "Only " + gov.getType().getLocale("affiliates")
                    + " may be kicked from a " + gov.getType().getName() + ".";
        }

        gov.removeMember(tgt);
        tgt.resetPower();
        player.sendMessage(MsgColor.SUCCESS + "The player " + tgt.getName()
                + " has been kicked out of the " + gov.getType().getName() + ".");
        return null;
    }

    public static String doGrant(MPlayer player, String division, String amt) {
        double amount;
        try {
            amount = Double.parseDouble(amt);
        } catch (NumberFormatException ex) {
            return "The amount you specified is an invalid number.";
        }

        if (!player.getPosition().isAtLeast(Position.OFFICER)) {
            return "You must be an officer or higher to do this.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a government.";
        }

        Division div = gov.getDivisionByName(division);
        if (div == null) {
            return "That division does not exist within your " + gov.getType().
                    getName() + ".";
        }

        if (!gov.transferWithCheck(div, amount)) {
            return "Your " + gov.getType().getName()
                    + " doesn't have enough money to perform this transaction.";
        }

        player.sendMessage(MsgColor.SUCCESS + amount + " " + MConfig.getString(
                "currency.namepl")
                + " have been granted to the " + gov.getType().getLocale(
                "division") + " " + div.getName() + ".");
        return null;
    }

    public static String doCreateDivision(MPlayer player, String name) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government!";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You do not have the proper rank to do this.";
        }

        name = name.trim();
        String validName = ValidationUtils.validateName(name);
        if (validName != null) {
            return validName;
        }

        if (!gov.canHaveMoreDivisions()) {
            return "The " + gov.getType().getName() + " has too many " + gov.
                    getType().getLocale("divisions")
                    + ". Make sure all of your " + gov.getType().getLocale(
                    "divisions") + " have greater than 5 players each.";
        }

        Division div = gov.createDivision().setManager(player.getName()).setName(
                name);

        gov.subtractMoney(MConfig.getDouble("prices.mafia.regimefound"));
        div.addMoney(MConfig.getDouble("mafia.regimestartup"));

        player.sendMessage(MsgColor.SUCCESS + "You have founded a " + gov.
                getType().getLocale("division") + " successfully.");
        return null;
    }

    public static String doSetManager(MPlayer player, String division,
            String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government!";
        }

        MPlayer manager = Mafiacraft.getOnlinePlayer(target);
        if (manager == null) {
            return "That player is either not online or doesn't exist.";
        }

        Division div = gov.getDivisionByName(division);
        if (div == null) {
            return "A " + gov.getType().getLocale("division")
                    + " with the name '" + division
                    + "' does not exist in your " + gov.getType().getName()
                    + ".";
        }

        div.setManager(manager);
        player.sendMessage(MsgColor.SUCCESS + "The capo for the regime ");
        return null;
    }

    public static String doClaim(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a government.";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You aren't allowed to claim land for your government.";
        }

        Chunk section = player.getChunk();
        District district = player.getDistrict();

        if (!district.canBeClaimed(section, gov)) {
            return "Your government isn't allowed to claim the given district.";
        }

        double price = district.getLandCost();
        if (!gov.hasEnough(price)) {
            return "Your government does not have enough money to purchase this land.";
        }

        gov.claim(section);
        gov.subtractMoney(price);

        player.sendMessage(MsgColor.SUCCESS
                + "You have successfully claimed the section " + district.
                getSectionName(section) + " for your " + gov.getType().getName()
                + ".");
        return null;
    }

    public static String doSetHq(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You aren't allowed to set the HQ of your government.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a government.";
        }

        Chunk section = player.getChunk();
        if (!Mafiacraft.getSectionOwner(section).equals(gov)) {
            return "The HQ must be specified within HQ land.";
        }

        double needed = MConfig.getDouble("prices.gov.sethq");
        if (!gov.hasEnough(needed)) {
            return "Your " + gov.getType().getName()
                    + " does not have enough money to set its HQ. (Costs "
                    + needed + ")";
        }

        gov.setHq(player.getBukkitEntity().getLocation()).subtractMoney(needed);
        player.sendMessage(MsgColor.SUCCESS + "Your " + gov.getType().getName()
                + " HQ has been set to your current location.");
        return null;
    }

    public static String doLeave(MPlayer player, GovType type) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a " + type.getName();
        }

        gov.removeMemberAndSucceed(player);

        player.sendMessage(MsgColor.SUCCESS + "You have left " + gov.getName()
                + ".");
        gov.broadcastMessage(MsgColor.INFO + player.getName() + " has left the "
                + gov.getType().getName() + ".");
        return null;
    }

    public static String doPromoteOfficer(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You do not have the proper rank to do this.";
        }

        if (!gov.canHaveMore(Position.OFFICER)) {
            return "Your government cannot have any more officers.";
        }

        MPlayer tgt = Mafiacraft.getOnlinePlayer(target);
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        if (!gov.isMember(tgt)) {
            return "You cannot promote a member that is not part of the " + gov.
                    getType().getName() + ".";
        }

        if (tgt.getPosition().isAtLeast(Position.OFFICER)) {
            return "That member is already either equal to or higher than an officer in rank.";
        }

        gov.removeMemberAndSucceed(tgt).addOfficer(tgt);

        player.sendMessage(MsgColor.SUCCESS + "The player " + tgt.getName()
                + " has been promoted to officer within your government.");
        return null;
    }

    public static String doDemoteOfficer(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You do not have the proper rank to do this.";
        }

        MPlayer tgt = Mafiacraft.getOnlinePlayer(target);
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        if (!gov.isMember(tgt)) {
            return "You cannot demote a member that is not part of the " + gov.
                    getType().getName() + ".";
        }

        if (tgt.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "That member is higher than an officer in rank.";
        }

        gov.removeMemberAndSucceed(tgt).addAffiliate(tgt);

        player.sendMessage(MsgColor.SUCCESS + "The player " + tgt.getName()
                + " has been demoted from officer within your government.");
        return null;
    }

}
