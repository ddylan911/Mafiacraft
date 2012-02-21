/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.MConfig;
import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.gov.Division;
import com.crimsonrpg.mafiacraft.gov.GovType;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.gov.Position;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import com.crimsonrpg.mafiacraft.util.ValidationUtils;
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
public class GovernmentCommand {
    public static void parseCmd(CommandSender sender, Command cmd, String label, String[] args, GovType type) {
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
            if (function.equalsIgnoreCase("who")) {
                result = doWho(player);
            } else if (function.equalsIgnoreCase("claim")) {
                result = doClaim(player);
            } else if (function.equalsIgnoreCase("hq")) {
                result = doHq(player);
            } else if (function.equalsIgnoreCase("sethq")) {
                result = doSetHq(player);
            } else {
                result = doHelp(player);
            }
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("who")) {
                result = doWho(player, largs.get(0));
            } else if (function.equalsIgnoreCase("invite")) {
                result = doInvite(player, largs.get(0));
            } else if (function.equalsIgnoreCase("kick")) {
                result = doKick(player, largs.get(0));
            } else if (function.equalsIgnoreCase("player")) {
                result = doPlayer(player, largs.get(0));
            } else if (function.equalsIgnoreCase("found")) {
                result = doFound(player, Joiner.on(' ').join(largs), type);
            } else {
                result = doHelp(player);
            }
        } else if (largs.size() < 3) {
            if (function.equalsIgnoreCase("allocate")) {
                result = doAllocate(player, largs.get(0), largs.get(1));
            } else if (function.equalsIgnoreCase("found")) {
                result = doFound(player, Joiner.on(' ').join(largs), type);
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
        player.sendMessage("TODO: help");
        //TODO: help
        return null;
    }

    public static String doFound(MPlayer player, String name, GovType type) {
        double balance = player.getMoney();
        double cost = MConfig.getDouble("prices.mafia.found");

        if (balance < cost) {
            return "You don't have enough money to do this. (Costs $" + cost + ")";
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
        Government founded = MafiacraftPlugin.getInstance().getGovernmentManager().createGovernment(name, type);
        if (!founded.addMember(player)) {
            return "Error adding. We can't do math.";
        }

        if (!founded.setPosition(player, Position.LEADER)) {
            return "The government specified has too many leaders. Speak to a server admin.";
        }

        player.sendMessage(MsgColor.SUCCESS + "You have successfully founded a new " + type.getName() + ".");
        return null;
    }

    public static String doPlayer(MPlayer player, String target) {
        MPlayer tgt = Mafiacraft.getPlayer(Bukkit.getPlayer(target));
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        player.sendMessage(MsgColor.INFO + "TODO: info for " + tgt.getDisplayName());
        //TODO: add stats
        return null;
    }

    public static String doWho(MPlayer player) {
        return doWho(player, player.getGovernment().getName());
    }

    public static String doWho(MPlayer player, String govName) {
        Government gov = Mafiacraft.getGovernmentManager().getGovernment(govName);
        if (gov == null) {
            return "The government you specified does not exist.";
        }

        player.sendMessage(MsgColor.INFO + "=== Info for " + gov.getName() + " ===");
        //TODO: add stats
        return null;
    }

    public static String doHq(MPlayer player) {
        Government gov = player.getGovernment();

        if (gov == null) {
            return "You are not part of a government.";
        }

        if (player.getPosition().compareTo(Position.WORKER) < 0) {
            return "Your position in your " + gov.getType().getName() + " is not high enough to use HQ teleport.";
        }

        Location hq = gov.getHq();
        if (hq == null) {
            return "Your " + gov.getType().getName() + " does not have a HQ set.";
        }

        //TODO: add countdown timer and teleport
        return null;
    }

    public static String doInvite(MPlayer player, String target) {
        MPlayer tgt = Mafiacraft.getPlayer(Bukkit.getPlayer(target));
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not part of a government.";
        }

        if (player.getPosition().compareTo(Position.WORKER) < 0) {
            return "Your position in your " + gov.getType().getName() + " is not high enough to invite people to your government.";
        }

        Government other = tgt.getGovernment();
        if (other != null) {
            if (other.equals(gov)) {
                return "The other player you specified is already in your " + gov.getType().getName() + ".";
            }
            return "The player you specified is already affiliated with a government.";
        }

        gov.dispatchInvite(player, tgt);
        player.sendMessage(MsgColor.INFO + "An invite to the mafia has been dispatched to " + tgt.getName() + ".");
        return null;
    }

    public static String doKick(MPlayer player, String target) {
        MPlayer tgt = Mafiacraft.getPlayer(Bukkit.getPlayer(target));
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not part of a government.";
        }

        if (player.getPosition().compareTo(Position.WORKER) < 0) {
            return "Your position in your " + gov.getType().getName() + " is not high enough to kick people from your government.";
        }

        Government other = tgt.getGovernment();
        if (other == null) {
            return "That player is not in a " + gov.getType().getName() + ".";
        }

        if (!other.equals(gov)) {
            return "That player is not in your " + gov.getType().getName() + ".";
        }

        if (!tgt.getPosition().equals(Position.AFFILIATE)) {
            return "Only " + gov.getType().getLocale("affiliates") + " may be kicked from a " + gov.getType().getName() + ".";
        }

        boolean removed = gov.removeMember(target);
        if (!removed) {
            return "Unknown error!";
        }
        player.sendMessage(MsgColor.SUCCESS + "The player " + tgt.getName() + " has been kicked out of the " + gov.getType().getName() + ".");
        return null;
    }

    public static String doAllocate(MPlayer player, String division, String amt) {
        double amount;
        try {
            amount = Double.parseDouble(amt);
        } catch (NumberFormatException ex) {
            return "The amount you specified is an invalid number.";
        }

        if (player.getPosition().compareTo(Position.OFFICER) < 0) {
            return "You must be an officer or higher to do this.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a government.";
        }

        Division div = gov.getDivisionByName(division);
        if (div == null) {
            return "That division does not exist within your " + gov.getType().getName() + ".";
        }

        if (!gov.transferWithCheck(div, amount)) {
            return "Your government doesn't have enough money to perform this transaction.";
        }

        player.sendMessage(MsgColor.SUCCESS + amount + " " + MConfig.getString("currency.namepl")
                + " have been allocated to the division " + div.getName() + ".");
        return null;
    }

    public static String doClaim(MPlayer player) {
        if (player.getPosition().compareTo(Position.VICE_LEADER) < 0) {
            return "You aren't allowed to claim land for your government.";
        }

        Chunk section = player.getChunk();
        District district = player.getDistrict();

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a government.";
        }

        if (!district.canBeClaimed(section, gov)) {
            return "Your government isn't allowed to claim the given district.";
        }

        gov.claim(section);
        player.sendMessage(MsgColor.SUCCESS + "You have successfully claimed the section " + district.getSectionName(section) + " for your " + gov.getType().getName() + ".");
        return null;
    }

    public static String doSetHq(MPlayer player) {
        if (player.getPosition().compareTo(Position.VICE_LEADER) < 0) {
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

        //TODO: take money from the mafia, idk how much

        gov.setHq(player.getBukkitEntity().getLocation());
        return null;
    }

}
