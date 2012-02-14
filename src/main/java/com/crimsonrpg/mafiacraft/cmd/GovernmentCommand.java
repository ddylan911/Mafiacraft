/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.gov.GovType;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.gov.Position;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class GovernmentCommand {
    public static void parseCmd(CommandSender sender, Command cmd, String label, String[] args, GovType type) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The person who made this plugin is too lazy to make them work in the console.");
            return;
        }
        
        MPlayer player = MafiacraftPlugin.getInstance().getPlayerManager().getPlayer((Player) sender);
        
        if (args.length < 1) {
            doHelp(player);
            return;
        }
        
        List<String> argList = new ArrayList<String>(Arrays.asList(args));
        
        String utility = args[0];
        
        if (utility.equalsIgnoreCase("found")) {
            if (argList.size() < 2){
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
        
        
        return null;
    }

}
