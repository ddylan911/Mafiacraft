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
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Holds government-related commands.
 */
public class GovernmentCommand {
    public static void parseCmd(CommandSender sender, Command cmd, String label, String[] args, GovType type) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The person who made this plugin is too lazy to make them work in the console.");
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) sender);

        if (args.length < 1) {
            doHelp(player);
            return;
        }

        List<String> argList = new ArrayList<String>(Arrays.asList(args));

        String utility = args[0];

        if (utility.equalsIgnoreCase("found")) {
            if (argList.size() < 2) {
                player.sendMessage(MsgColor.ERROR + "You need to specify a name for your " + type.getName() + ".");
                return;
            }
            argList.remove(0);
            doFound(player, Joiner.on(' ').join(argList), type);
        }

        return;
    }

    public static String doHelp(MPlayer player) {
        player.sendMessage("TODO: help");
        //TODO: help
        return null;
    }

    public static String doFound(MPlayer player, String name, GovType type) {
        double balance = player.getMoney();
        double cost = MafiacraftPlugin.getInstance().getConfig().getDouble("prices.mafia.found", 1000000.0);

        if (balance < cost) {
            return "You don't have enough money to do this. (Costs $" + cost + ")";
        }

        if (player.getGovernment() != null) {
            return "You are already in a government!";
        }

        //Found the government
        Government founded = MafiacraftPlugin.getInstance().getGovernmentManager().createGovernment(name, type);
        if (!founded.addMember(player).setPosition(player, Position.LEADER)) {
            return "The government specified has too many leaders. Speak to a server admin.";
        }

        return null;
    }

    public static String doPlayer(MPlayer player, MPlayer target) {
        player.sendMessage(MsgColor.INFO + "TODO: info for " + target.getDisplayName());
        //TODO: add stats
        return null;
    }

    public static String doWho(MPlayer player) {
        return doWho(player, player.getGovernment());
    }

    public static String doWho(MPlayer player, Government government) {
        player.sendMessage(MsgColor.INFO + "=== Info for " + government.getName() + " ===");
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

        //TODO: teleport to gov HQ.
        return null;
    }

    public static String doInvite(MPlayer player, MPlayer target) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not part of a government.";
        }

        if (player.getPosition().compareTo(Position.WORKER) < 0) {
            return "Your position in your " + gov.getType().getName() + " is not high enough to invite people to your government.";
        }

        Government other = target.getGovernment();
        if (other != null) {
            if (other.equals(gov)) {
                return "The other player you specified is already in your " + gov.getType().getName() + ".";
            }
            return "The player you specified is already affiliated with a government.";
        }

        gov.dispatchInvite(player, target);
        player.sendMessage(MsgColor.INFO + "An invite to the mafia has been dispatched to " + target.getName() + ".");
        return null;
    }

    public static String doKick(MPlayer player, MPlayer target) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not part of a government.";
        }

        if (player.getPosition().compareTo(Position.WORKER) < 0) {
            return "Your position in your " + gov.getType().getName() + " is not high enough to kick people from your government.";
        }

        Government other = target.getGovernment();
        if (other == null) {
            return "That player is not in a " + gov.getType().getName() + ".";
        }

        if (!other.equals(gov)) {
            return "That player is not in your " + gov.getType().getName() + ".";
        }

        if (!target.getPosition().equals(Position.AFFILIATE)) {
            return "Only " + gov.getType().getLocale("affiliates") + " may be kicked from a " + gov.getType().getName() + ".";
        }

        boolean removed = gov.removeMember(target);
        if (!removed) {
            return "Unknown error!";
        }
        player.sendMessage(MsgColor.SUCCESS + "The player " + target.getName() + " has been kicked out of the " + gov.getType().getName() + ".");
        return null;
    }

    public static String doAllocate(MPlayer player, String division, double amount) {
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
        Chunk section = player.getChunk();
        District district = player.getDistrict();
        
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a government.";
        }
        
        if (!district.canBeClaimed(section, gov)) {
            return "Your government isn't allowed to claim the given district.";
        }
        
        
        return null;
    }
}
