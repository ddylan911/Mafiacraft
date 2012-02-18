/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.gov.Division;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.gov.Position;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import com.crimsonrpg.mafiacraft.player.SessionStore;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Dylan
 */
public class DivisionCommand {

    private static SessionStore ss = new SessionStore();

    public static void parseCmd(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR + "You can not use this command through console.");
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) sender);



        List<String> argList = new ArrayList<String>(Arrays.asList(args));

        String option = args[0];
        String result = null;

        if (option.equalsIgnoreCase("accept")) {

            result = doAccept(player, null);
        } else if (option.equalsIgnoreCase("desc")) {
            if (args.length < 2) {
                player.sendMessage(MsgColor.ERROR + "You need a description too.");
                return;
            }
            argList.remove(0);

            result = doDesc(player, Joiner.on(' ').join(argList));
        } else if (option.equalsIgnoreCase("name")) {
            result = doName(player, argList.get(1));
        } else if (option.equalsIgnoreCase("invite")) {
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doAccept(MPlayer player, Division div) {
        Government gov = player.getGovernment();
        if (player.getDivision() != null) {
            return "You are already in a " + gov.getType().getLocale("division")
                    + ", you must leave it first.";
        }

        if (!(ss.getBoolean(player.getName() + ".inv." + div.getName(), false) == true)) {

            return "No one has invited you.";
        }
        div.addWorker(player);
        player.sendMessage(MsgColor.SUCCESS + "You have succesfully joined " + div.getName());
        return null;
    }

    public static String doInvite(MPlayer player, MPlayer invited) {
        if (!player.getPosition().equals(Position.MANAGER)) {
            return "You do not have the proper rank.";
        }
        ss.setData(invited.getName() + ".inv." + player.getDivision().getName(), true);
        return null;
    }

    public static String doName(MPlayer player, String name) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }
        if (player.getDivision() == null) {
            return "You are not part of a " + gov.getType().getLocale("division") + ".";
        }
        return null;
    }

    public static String doDesc(MPlayer player, String desc) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }
        Division div = player.getDivision();
        if (div == null) {
            return "You are not in a " + gov.getType().getLocale("division") + ".";
        }
        if (!player.getPosition().equals(Position.MANAGER)) {
            return "You do not have the proper rank to do this.";
        }
        div.setDescription(desc);
        player.sendMessage(MsgColor.SUCCESS + "You have changed your " + gov.getType().getLocale("division") + "'s description.");
        return null;
    }
}
