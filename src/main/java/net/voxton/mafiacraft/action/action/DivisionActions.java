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
package net.voxton.mafiacraft.action.action;

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.gov.Division;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.gov.Position;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import com.google.common.base.Joiner;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import net.voxton.mafiacraft.action.ActionType;
import net.voxton.mafiacraft.action.PlayerActions;
import net.voxton.mafiacraft.geo.MPoint;
import net.voxton.mafiacraft.geo.Section;
import net.voxton.mafiacraft.gov.GovType;
import net.voxton.mafiacraft.help.MenuType;

/**
 * Division related commands.
 */
public final class DivisionActions extends PlayerActions {

    private final GovType type;

    public DivisionActions(GovType type) {
        this.type = type;
    }

    @Override
    protected String performActionCommand(MPlayer performer,
            String action,
            List<String> args) {
        String result = null;

        if (args.size() < 1) {
            if (action.equalsIgnoreCase("accept")) {
                result = doAccept(performer);
            } else if (action.equalsIgnoreCase("claim")) {
                result = doClaim(performer);
            } else if (action.equalsIgnoreCase("unclaim")) {
                result = doUnclaim(performer);
            } else if (action.equalsIgnoreCase("help")) {
                result = doHelp(performer, type);
            } else {
                result = doHelp(performer, type);
            }
        } else if (args.size() < 2) {
            if (action.equalsIgnoreCase("create")) {
                result = doCreate(performer, args.get(0));
            } else if (action.equalsIgnoreCase("kick")) {
                result = doKick(performer, args.get(0));
            } else if (action.equalsIgnoreCase("name")) {
                result = doName(performer, args.get(0));
            } else if (action.equalsIgnoreCase("invite")) {
                result = doInvite(performer, args.get(0));
            } else if (action.equalsIgnoreCase("help")) {
                result = doHelp(performer, args.get(0), type);
            } else {
                result = doHelp(performer, type);
            }
        } else {
            result = doDesc(performer, Joiner.on(' ').join(args));
        }

        return result;
    }

    /**
     * Performs help.
     * 
     * @param player The player performing help.
     * @param type The type of government.
     * @return The errors.
     */
    public String doHelp(MPlayer player, GovType type) {
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
    public String doHelp(MPlayer player, String arg, GovType type) {
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
    public String doKick(MPlayer player, String target) {
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
    public String doCreate(MPlayer player, String name) {
        if (type.equals(GovType.MAFIA)) {
            return ActionType.MAFIA.doCreateDivision(player, name);
        } else if (type.equals(GovType.POLICE)) {
            return ActionType.POLICE.doCreateDivision(player, name);
        }

        return "THE WORLD HAS ENDED";
    }

    /**
     * Accepts an invite if there is one.
     *
     * @param player
     * @return
     */
    public String doAccept(MPlayer player) {
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
    public String doClaim(MPlayer player) {
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

        Section chunk = player.getSection();
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
    public String doInvite(MPlayer player, String target) {
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
    public String doName(MPlayer player, String name) {
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

    public String doUnclaim(MPlayer player) {
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

        div.unclaim(player.getSection());
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
    public String doDesc(MPlayer player, String desc) {
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

    public String doHq(MPlayer player) {
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

        MPoint hq = div.getHq();
        if (hq == null) {
            return "Your " + div.getGovernment().getType().getLocale("division")
                    + " does not have a HQ set.";
        }

        //Teleport.
        player.teleportWithCountdown(hq);
        return null;
    }

    public String doSetHq(MPlayer player) {
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

        Section section = player.getSection();
        if (!Mafiacraft.getSectionOwner(section).equals(div)) {
            return "The HQ must be specified within HQ land.";
        }

        //TODO: take money from the mafia, idk how much

        div.setHq(player.getPoint());
        player.sendMessage(MsgColor.SUCCESS + "Your " + div.getGovernment().
                getType().getLocale("division") + " HQ has been set.");
        return null;
    }

}
