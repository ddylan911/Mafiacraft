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

import net.voxton.mafiacraft.geo.City;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.geo.DistrictType;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.util.ValidationUtils;
import com.google.common.base.Joiner;
import java.util.List;
import net.voxton.mafiacraft.action.PlayerActions;
import net.voxton.mafiacraft.geo.MPoint;
import net.voxton.mafiacraft.help.MenuType;
import net.voxton.mafiacraft.util.StringUtils;

/**
 * Commands relating to districts.
 */
public final class DistrictActions extends PlayerActions {

    @Override
    protected String performActionCommand(MPlayer performer, String action,
            List<String> args) {
        String result = null;

        if (args.size() < 1) {
            if (action.equalsIgnoreCase("setbus")) {
                result = doSetBus(performer);
            } else if (action.equalsIgnoreCase("info")) {
                result = doInfo(performer);
            } else if (action.equalsIgnoreCase("claimgrid")) {
                result = doClaimGrid(performer);
            } else {
                result = doHelp(performer, action);
            }
        } else if (args.size() < 2) {
            if (action.equalsIgnoreCase("zone")) {
                result = doZone(performer, args.get(0));
            } else if (action.equalsIgnoreCase("setcost")) {
                result = doSetCost(performer, args.get(0));
            } else if (action.equalsIgnoreCase("help")) {
                result = doHelp(performer, args.get(0));
            } else {
                result = doHelp(performer, action);
            }
        } else {
            if (action.equalsIgnoreCase("desc")) {
                String desc = Joiner.on(' ').join(args);
                result = doDesc(performer, desc);
            } else {
                result = doHelp(performer, action);
            }
        }

        return result;
    }

    public String doDesc(MPlayer player, String description) {
        if (!player.hasPermission("mafiacraft.mod")) {
            return player.getLocale().localize("action.general.not-allowed");
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return player.getLocale().localize(
                    "command.district.this-not-associated");
        }

        if (city.isMember(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-member.district.desc");
        }

        description = description.trim();
        String result = ValidationUtils.validateDescription(description);
        if (result != null) {
            return result;
        }

        //Set the description
        district.setDescription(description);

        //Success!
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.district.desc", description));
        return null;
    }

    public String doHelp(MPlayer player) {
        MenuType.DISTRICT.doHelp(player);
        return null;
    }

    public String doHelp(MPlayer player, String arg) {
        MenuType.DISTRICT.doHelp(player, arg);
        return null;
    }

    public String doInfo(MPlayer player) {
        //TODO add real info
        player.sendMessage("You are in the district " + player.getDistrict().
                getNameInChat());
        return null;
    }

    public String doSetBus(MPlayer player) {
        if (!player.hasPermission("mafiacraft.mod")) {
            return player.getLocale().localize("action.general.not-allowed");
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return player.getLocale().localize(
                    "command.district.this-not-associated");
        }

        if (city.isMember(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-member.district.setbus");
        }

        MPoint bus = player.getPoint();
        district.setBusStop(bus);

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.district.bus-set", district.getNameInChat()));
        return null;
    }

    public String doZone(MPlayer player, String typeString) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("action.general.not-citizen");
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return player.getLocale().localize(
                    "command.district.this-not-associated");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.district.must-be-mayor.zone");
        }

        DistrictType type = DistrictType.fromString(typeString);
        if (type == null) {
            return player.getLocale().localize("action.district.no-such-type",
                    typeString);
        }

        if (type.equals(DistrictType.UNEXPLORED)) {
            return player.getLocale().localize(
                    "command.district.cannot-unexplore");
        }

        district.resetOwnerships().setType(type);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.district.zoned", type.niceName()));
        return null;
    }

    public String doClaimGrid(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("action.general.not-citizen");
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return player.getLocale().localize(
                    "command.district.this-not-associated");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.district.must-be-mayor.claim-grid");
        }

        boolean check = city.claimGridAndCheck(district);
        if (!check) {
            return player.getLocale().localize(
                    "command.district.cannot-claim-grid");
        }

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.district.claimed-grid"));
        return null;
    }

    public String doSetCost(MPlayer player, String amount) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("action.general.not-citizen");
        }

        District district = player.getDistrict();
        City city = district.getCity();
        if (city == null) {
            return player.getLocale().localize(
                    "command.district.this-not-associated");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.district.must-be-mayor.set-cost");
        }

        double cost = 0;
        try {
            cost = Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            return player.getLocale().localize("action.general.invalid-number",
                    amount);
        }

        district.setLandCost(cost);

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.cost-set", StringUtils.formatCurrency(cost)));
        return null;
    }
}
