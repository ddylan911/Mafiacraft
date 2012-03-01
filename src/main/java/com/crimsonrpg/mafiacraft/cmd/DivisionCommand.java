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
import com.crimsonrpg.mafiacraft.util.TPCD;
import com.crimsonrpg.mafiacraft.util.ValidationUtils;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Division related commands.
 */
public class DivisionCommand {
    public static void parseCmd(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR + "You can not use this command through console.");
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(MsgColor.ERROR + "You need a utility!");
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) sender);

        List<String> argList = new ArrayList<String>(Arrays.asList(args));

        String option = args[0];
        String result = null;

        if (option.equalsIgnoreCase("accept")) {
            result = doAccept(player);
        } else if (option.equalsIgnoreCase("claim")) {
            result = doClaim(player);
        } else if (option.equalsIgnoreCase("create")) {
            if (args.length < 2) {
                player.sendMessage(MsgColor.ERROR + "You need a name too.");
                return;
            }
            result = doCreate(player, argList.get(1));
        } else if (option.equalsIgnoreCase("desc")) {
            if (args.length < 2) {
                player.sendMessage(MsgColor.ERROR + "You need a description too.");
                return;
            }
            argList.remove(0);
            result = doDesc(player, Joiner.on(' ').join(argList));
        } else if (option.equalsIgnoreCase("kick")) {
            if (args.length < 2) {
                player.sendMessage(MsgColor.ERROR + "You need a player name too.");
                return;
            }
            result = doKick(player, Mafiacraft.getPlayerManager().getPlayer(argList.get(1)));
        } else if (option.equalsIgnoreCase("name")) {
            if (args.length < 2) {
                player.sendMessage(MsgColor.ERROR + "You need a divison name too.");
                return;
            }
            result = doName(player, argList.get(1));
        } else if (option.equalsIgnoreCase("invite")) {
            if (args.length < 2) {
                player.sendMessage(MsgColor.ERROR + "You need a player name too.");
                return;
            }
            result = doInvite(player, Mafiacraft.getPlayerManager().getPlayer(argList.get(1)));
        } else if (option.equalsIgnoreCase("unclaim")) {
            result = doUnclaim(player);
        }
        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    /**
     * Takes in a kickee and a kicked, and checks the proper st00f, then kicks
     * the kicked.
     *
     * @param player
     * @param kicked
     * @return
     */
    public static String doKick(MPlayer player, MPlayer kicked) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }
        if (player.getDivision() == null) {
            return "You are not part of a " + gov.getType().getLocale("division") + ".";
        }
        if (!player.getPosition().equals(Position.MANAGER)) {
            return "You do not have the proper rank to do this.";
        }
        boolean remove = player.getDivision().remove(kicked.getName());
        if (remove == false) {
            return "You can not kick this player out!";
        }
        player.sendMessage(MsgColor.SUCCESS + "You have successfully kicked out " + kicked.getName() + ".");
        return null;
    }

    /**
     * Creates a division with the given player and name.
     *
     * @param player
     * @param name
     * @return
     */
    public static String doCreate(MPlayer player, String name) {
        return GovernmentCommand.doCreateDivision(player, name);
    }

    /**
     * Accepts an invite if there is one.
     *
     * @param player
     * @return
     */
    public static String doAccept(MPlayer player) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }
        if (player.getDivision() != null) {
            return "You are already in a " + gov.getType().getLocale("division")
                    + ", you must leave it first.";
        }
        String divName = null;
        for (Division div : gov.getDivisions()) {
            if (player.getSessionStore().getObject(player.getName() + ".inv." + div.getName()) != null) {
                divName = div.getName();
                continue;
            }
        }
        Division div = gov.getDivisionByName(divName);
        if (!(player.getSessionStore().getBoolean(player.getName() + ".inv." + div.getName(), false) == true)) {
            return "No one has invited you.";
        }
        player.getSessionStore().resetData(player.getName() + ".inv." + divName);
        div.addWorker(player);
        player.sendMessage(MsgColor.SUCCESS + "You have succesfully joined " + div.getName());
        return null;
    }

    /**
     * Claims the current chunk you are in if it is claimable.
     *
     * @param player
     * @return
     */
    public static String doClaim(MPlayer player) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }
        Division div = player.getDivision();
        if (div == null) {
            return "You are not part of a " + gov.getType().getLocale("division") + ".";
        }
        Chunk chunk = player.getChunk();
        div.claim(chunk);
        player.sendMessage(MsgColor.SUCCESS + "You have claimed this chunk successfully.");
        return null;
    }

    /**
     * Invites the given player to the division.
     *
     * @param player
     * @param invited
     * @return
     */
    public static String doInvite(MPlayer player, MPlayer invited) {
        if (!player.getPosition().equals(Position.MANAGER)) {
            return "You do not have the proper rank.";
        }
        player.getSessionStore().setData(invited.getName() + ".inv." + player.getDivision().getName(), true);
        player.sendMessage(MsgColor.SUCCESS + "You invited " + invited.getName() + ".");
        return null;
    }

    /**
     * Names the division the given name.
     *
     * @param player
     * @param name
     * @return
     */
    public static String doName(MPlayer player, String name) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }
        Division div = player.getDivision();
        if (div == null) {
            return "You are not part of a " + gov.getType().getLocale("division") + ".";
        }
        if (!player.getPosition().equals(Position.MANAGER)) {
            return "You do not have the proper rank to do this.";
        }
        div.setName(name);
        player.sendMessage(MsgColor.SUCCESS + "You have succesfully set the name.");
        return null;
    }

    public static String doUnclaim(MPlayer player) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }

        Division div = player.getDivision();
        if (div == null) {
            return "You are not part of a " + gov.getType().getLocale("division") + ".";
        }

        if (!player.getPosition().isAtLeast(Position.MANAGER)) {
            return "You do not have the proper rank to do this.";
        }

        div.unclaim(player.getChunk());
        player.sendMessage(MsgColor.SUCCESS + "You have successfully unclaimed this section.");
        return null;
    }

    /**
     * Sets the description of the division.
     *
     * @param player
     * @param desc
     * @return
     */
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

    public static String doHq(MPlayer player) {
        Division div = player.getDivision();

        if (div == null) {
            return "You are not part of a " + div.getGovernment().getType().getLocale("division") + ".";
        }

        if (player.getPosition().isAtLeast(Position.WORKER)) {
            return "Your position in your " + div.getGovernment().getType().getLocale("division") + " is not high enough to use HQ teleport.";
        }

        Location hq = div.getHq();
        if (hq == null) {
            return "Your " + div.getGovernment().getType().getLocale("division") + " does not have a HQ set.";
        }

        //Teleport.
        TPCD.makeCountdown(Mafiacraft.getPlugin(), 10, TPCD.Type.GOVHQ, player.getBukkitEntity(), hq);
        return null;
    }

    public static String doSetHq(MPlayer player) {
        Division div = player.getDivision();
        if (div == null) {
            return "You are not part of a government.";
        }

        if (player.getPosition().isAtLeast(Position.MANAGER)) {
            return "You aren't allowed to set the HQ of your " + div.getGovernment().getType().getLocale("division") + ".";
        }

        Chunk section = player.getChunk();
        if (!Mafiacraft.getSectionOwner(section).equals(div)) {
            return "The HQ must be specified within HQ land.";
        }

        //TODO: take money from the mafia, idk how much

        div.setHq(player.getBukkitEntity().getLocation());
        player.sendMessage(MsgColor.SUCCESS + "Your " + div.getGovernment().getType().getLocale("division") + " HQ has been set.");
        return null;
    }

}
