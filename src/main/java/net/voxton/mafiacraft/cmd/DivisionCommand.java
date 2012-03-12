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

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.gov.Division;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.gov.Position;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.util.TPCD;
import com.google.common.base.Joiner;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.voxton.mafiacraft.gov.GovType;
import net.voxton.mafiacraft.help.MenuType;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Division related commands.
 */
public final class DivisionCommand {

    public static void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args, GovType type) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR
                    + "You can not use this command through console.");
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) sender);

        if (args.length < 1) {
            doHelp(player, type);
            return;
        }

        //Get the function we want to do.
        String function = args[0];
        List<String> largs = new ArrayList<String>(Arrays.asList(args));
        largs.remove(0);

        String result = null;

        if (largs.size() < 1) {
            if (function.equalsIgnoreCase("accept")) {
                result = doAccept(player);
            } else if (function.equalsIgnoreCase("claim")) {
                result = doClaim(player);
            } else if (function.equalsIgnoreCase("unclaim")) {
                result = doUnclaim(player);
            } else if (function.equalsIgnoreCase("help")) {
                result = doHelp(player, type);
            } else {
                result = doHelp(player, type);
            }
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("create")) {
                result = doCreate(player, largs.get(0));
            } else if (function.equalsIgnoreCase("kick")) {
                result = doKick(player, largs.get(0));
            } else if (function.equalsIgnoreCase("name")) {
                result = doName(player, largs.get(0));
            } else if (function.equalsIgnoreCase("invite")) {
                result = doInvite(player, largs.get(0));
            } else if (function.equalsIgnoreCase("help")) {
                result = doHelp(player, largs.get(0), type);
            } else {
                result = doHelp(player, type);
            }
        } else {
            result = doDesc(player, Joiner.on(' ').join(largs));
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    /**
     * Performs help.
     * 
     * @param player The player performing help.
     * @param type The type of government.
     * @return The errors.
     */
    public static String doHelp(MPlayer player, GovType type) {
        if (type.equals(GovType.MAFIA)) {
            MenuType.REGIME.doHelp(player);
        } else if (type.equals(GovType.POLICE)) {
            MenuType.SQUAD.doHelp(player);
        }
        return null;
    }

    /**
     * Performs help.
     * 
     * @param player The player performing help.
     * @param arg The help argument.
     * @param type The type of government.
     * @return The errors.
     */
    public static String doHelp(MPlayer player, String arg, GovType type) {
        if (type.equals(GovType.MAFIA)) {
            MenuType.REGIME.doHelp(player, arg);
        } else if (type.equals(GovType.POLICE)) {
            MenuType.SQUAD.doHelp(player, arg);
        }
        return null;
    }

    /**
     * Takes in a kickee and a kicked, and checks the proper st00f, then kicks
     * the kicked.
     *
     * @param player
     * @param tgt
     * @return
     */
    public static String doKick(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        MPlayer tgt = Mafiacraft.getOnlinePlayer(target);
        if (tgt == null) {
            return "The player you have specified is either offline or does not exist.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }
        if (player.getDivision() == null) {
            return "You are not part of a "
                    + gov.getType().getLocale("division") + ".";
        }
        if (!player.getPosition().equals(Position.MANAGER)) {
            return "You do not have the proper rank to do this.";
        }
        boolean remove = player.getDivision().remove(tgt.getName());
        if (remove == false) {
            return "You can not kick this player out!";
        }
        player.sendMessage(MsgColor.SUCCESS
                + "You have successfully kicked out " + tgt.getName() + ".");
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
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

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
            if (player.getSessionStore().getObject(player.getName() + ".inv."
                    + div.getName()) != null) {
                divName = div.getName();
                continue;
            }
        }
        Division div = gov.getDivisionByName(divName);
        if (!(player.getSessionStore().getBoolean(player.getName() + ".inv."
                + div.getName(), false) == true)) {
            return "No one has invited you.";
        }
        player.getSessionStore().resetData(player.getName() + ".inv." + divName);
        div.addWorker(player);
        player.sendMessage(MsgColor.SUCCESS + "You have succesfully joined "
                + div.getName());
        return null;
    }

    /**
     * Claims the current chunk you are in if it is claimable.
     *
     * @param player
     * @return
     */
    public static String doClaim(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }

        Division div = player.getDivision();
        if (div == null) {
            return "You are not part of a "
                    + gov.getType().getLocale("division") + ".";
        }

        Position pos = player.getPosition();
        if (!pos.isAtLeast(Position.MANAGER)) {
            return "You are not the " + gov.getType().getLocale("manager")
                    + " of this " + gov.getType().getName() + ".";
        }

        District dis = player.getDistrict();
        double price = dis.getLandCost();
        if (!div.hasEnough(price)) {
            return "Your " + gov.getType().getLocale("division")
                    + " does not have enough money to buy a plot in this district. "
                    + "(Costs $" + NumberFormat.getCurrencyInstance(
                    Locale.ENGLISH).
                    format(price) + ")";
        }

        Chunk chunk = player.getChunk();
        if (!dis.canBeClaimed(chunk, div)) {
            return "You aren't allowed to buy land here.";
        }

        div.claim(chunk);
        div.subtractMoney(price);

        player.sendMessage(MsgColor.SUCCESS
                + "You have claimed this chunk successfully.");
        return null;
    }

    /**
     * Invites the given player to the division.
     *
     * @param player
     * @param target
     * @return
     */
    public static String doInvite(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        MPlayer tgt = Mafiacraft.getOnlinePlayer(target);
        if (tgt == null) {
            return "The player you have specified is either offline or does not exist.";
        }

        if (!player.getPosition().equals(Position.MANAGER)) {
            return "You do not have the proper rank.";
        }
        player.getSessionStore().setData(tgt.getName() + ".inv." + player.
                getDivision().getName(), true);
        player.sendMessage(MsgColor.SUCCESS + "You invited " + tgt.getName()
                + ".");
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
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }
        Division div = player.getDivision();
        if (div == null) {
            return "You are not part of a "
                    + gov.getType().getLocale("division") + ".";
        }
        if (!player.getPosition().equals(Position.MANAGER)) {
            return "You do not have the proper rank to do this.";
        }
        div.setName(name);
        player.sendMessage(MsgColor.SUCCESS
                + "You have succesfully set the name.");
        return null;
    }

    public static String doUnclaim(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }

        Division div = player.getDivision();
        if (div == null) {
            return "You are not part of a "
                    + gov.getType().getLocale("division") + ".";
        }

        if (!player.getPosition().isAtLeast(Position.MANAGER)) {
            return "You do not have the proper rank to do this.";
        }

        div.unclaim(player.getChunk());
        player.sendMessage(MsgColor.SUCCESS
                + "You have successfully unclaimed this section.");
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
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }
        Division div = player.getDivision();
        if (div == null) {
            return "You are not in a " + gov.getType().getLocale("division")
                    + ".";
        }
        if (!player.getPosition().equals(Position.MANAGER)) {
            return "You do not have the proper rank to do this.";
        }
        div.setDescription(desc);
        player.sendMessage(MsgColor.SUCCESS + "You have changed your " + gov.
                getType().getLocale("division") + "'s description.");
        return null;
    }

    public static String doHq(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Division div = player.getDivision();

        if (div == null) {
            return "You are not part of a " + div.getGovernment().getType().
                    getLocale("division") + ".";
        }

        if (!player.getPosition().isAtLeast(Position.WORKER)) {
            return "Your position in your " + div.getGovernment().getType().
                    getLocale("division")
                    + " is not high enough to use HQ teleport.";
        }

        Location hq = div.getHq();
        if (hq == null) {
            return "Your " + div.getGovernment().getType().getLocale("division")
                    + " does not have a HQ set.";
        }

        //Teleport.
        TPCD.makeCountdown(10, TPCD.Type.GOVHQ, player.
                getBukkitEntity(), hq);
        return null;
    }

    public static String doSetHq(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Division div = player.getDivision();
        if (div == null) {
            return "You are not part of a government.";
        }

        if (!player.getPosition().isAtLeast(Position.MANAGER)) {
            return "You aren't allowed to set the HQ of your " + div.
                    getGovernment().getType().getLocale("division") + ".";
        }

        Chunk section = player.getChunk();
        if (!Mafiacraft.getSectionOwner(section).equals(div)) {
            return "The HQ must be specified within HQ land.";
        }

        //TODO: take money from the mafia, idk how much

        div.setHq(player.getBukkitEntity().getLocation());
        player.sendMessage(MsgColor.SUCCESS + "Your " + div.getGovernment().
                getType().getLocale("division") + " HQ has been set.");
        return null;
    }

}
