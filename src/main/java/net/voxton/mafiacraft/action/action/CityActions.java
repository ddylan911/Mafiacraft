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

import net.voxton.mafiacraft.action.ActionPerformer;
import net.voxton.mafiacraft.config.Config;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.City;
import net.voxton.mafiacraft.geo.MWorld;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.util.ValidationUtils;
import java.util.List;
import net.voxton.mafiacraft.action.PlayerActions;
import net.voxton.mafiacraft.geo.MPoint;
import net.voxton.mafiacraft.geo.Section;
import net.voxton.mafiacraft.help.MenuType;
import net.voxton.mafiacraft.util.StringUtils;

/**
 * Contains all city-related commands.
 */
public final class CityActions extends PlayerActions {

    @Override
    public String performActionCommand(MPlayer performer, String action,
            List<String> args) {

        String result;
        if (args.size() < 1) {
            if (action.equalsIgnoreCase("annex")) {
                result = doAnnex(performer);
            } else if (action.equalsIgnoreCase("funds")) {
                result = doFunds(performer);
            } else if (action.equalsIgnoreCase("spawn")) {
                result = doSpawn(performer);
            } else if (action.equalsIgnoreCase("setspawn")) {
                result = doSetSpawn(performer);
            } else if (action.equalsIgnoreCase("unannex")) {
                result = doUnannex(performer);
            } else if (action.equalsIgnoreCase("disband")) {
                result = doDisband(performer);
            } else if (action.equalsIgnoreCase("claim")) {
                result = doClaim(performer);
            } else if (action.equalsIgnoreCase("unclaim")) {
                result = doUnclaim(performer);
            } else {
                result = doHelp(performer, action);
            }
        } else if (args.size() < 2) {
            if (action.equalsIgnoreCase("bus")) {
                result = doBus(performer, args.get(0));
            } else if (action.equalsIgnoreCase("found")) {
                result = doFound(performer, args.get(0));
            } else if (action.equalsIgnoreCase("rename")) {
                result = doRename(performer, args.get(0));
            } else if (action.equalsIgnoreCase("deposit")) {
                result = doDeposit(performer, args.get(0));
            } else if (action.equalsIgnoreCase("withdraw")) {
                result = doWithdraw(performer, args.get(0));
            } else if (action.equalsIgnoreCase("setchief")) {
                result = doSetChief(performer, args.get(0));
            } else if (action.equalsIgnoreCase("setassistant")) {
                result = doSetAssistant(performer, args.get(0));
            } else {
                result = doHelp(performer, action);
            }
        } else if (args.size() < 3) {
            if (action.equalsIgnoreCase("makepolice")) {
                result = doMakePolice(performer, args.get(0), args.get(1));
            } else {
                result = doHelp(performer, action);
            }
        } else {
            result = doHelp(performer, action);
        }

        return result;
    }

    public String doHelp(MPlayer player) {
        MenuType.CITY.doHelp(player);
        return null;
    }

    public String doHelp(MPlayer player, String arg) {
        MenuType.CITY.doHelp(player, arg);
        return null;
    }

    public String doFound(MPlayer player, String name) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        MWorld cw = player.getCityWorld();
        City capital = cw.getCapital();
        if (capital != null) {
            return player.getLocale().localize(
                    "command.city.capital-established");
        }

        double balance = player.getMoney();
        double foundCost = Config.getDouble("city.foundcost");

        if (balance < foundCost) {
            return player.getLocale().localize("command.city.no-money.found",
                    StringUtils.formatCurrency(foundCost));
        }

        if (!ValidationUtils.validateName(name)) {
            return player.getLocale().localize("command.city.invalid-name");
        }

        if (Mafiacraft.getCityManager().cityExists(name)) {
            return player.getLocale().localize("command.city.name-taken");
        }

        //Found a city
        Section sample = player.getSection();
        District district = Mafiacraft.getDistrict(sample);
        City city =
                Mafiacraft.getCityManager().foundCity(player, name, district);
        player.transferMoney(city, foundCost);

        //Notify
        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.city.founded"));
        return null;
    }

    public String doSetSpawn(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMember(player)) {
            return player.getLocale().localize("command.city.not-allowed");
        }

        city.setSpawnPoint(player.getPoint());
        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.city.set-spawn"));
        return null;
    }

    public String doSpawn(MPlayer player) {
        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        MPoint spawn = city.getSpawnPoint();
        if (spawn == null) {
            return player.getLocale().localize("command.city.no-spawn");
        }

        int citySpawnTime = Config.getInt("warmup.cityspawn");
        player.teleportWithCountdown(spawn);
        return null;
    }

    public String doAnnex(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCityWorld().getCapital();
        if (city == null) {
            return player.getLocale().localize("command.city.none-in-world");
        }

        District district = player.getDistrict();
        if (district.getCity() != null) {
            return player.getLocale().localize(
                    "command.city.district-unavailable");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.district.must-be-mayor.annex");
        }

        double cost = Config.getDouble("prices.city.annex");
        if (!city.hasEnough(cost)) {
            return player.getLocale().localize("command.city.no-money.annex",
                    StringUtils.formatCurrency(cost));
        }

        city.subtractMoney(cost);
        city.attachNewDistrict(district);
        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.district.claimed"));
        return null;
    }

    public String doUnannex(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        District district = player.getDistrict();
        if (district.getCity() == null) {
            return player.getLocale().localize("command.district.not-associated");
        }

        if (!district.getCity().equals(city)) {
            return player.getLocale().localize("command.district.not-owned");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.district.must-be-mayor.unannex");
        }

        district.detachFromCity();
        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.district.unclaimed"));
        return null;
    }

    public String doRename(MPlayer player, String name) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-mayor.rename");
        }

        if (!ValidationUtils.validateName(name)) {
            return player.getLocale().localize("command.city.invalid-name");
        }

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.renamed", city.getName(), name));
        city.setName(name);
        return null;
    }

    public String doFunds(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMember(player)) {
            return player.getLocale().localize(
                    "command.city.must-be.member.funds");
        }

        double funds = city.getMoney();
        String fundsStr = StringUtils.formatCurrency(funds);
        player.sendMessage(MsgColor.INFO + player.getLocale().localize(
                "command.city.funds", city.getOwnerName(), fundsStr));
        return null;
    }

    public String doDisband(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-mayor.disband");
        }

        city.disband(); //Holy crap!
        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize("command.city.disbanded"));
        return null;
    }

    public String doBus(MPlayer player, String districtName) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        int busDist = Config.getInt("district.bus-max-distance");

        District thisDist = player.getDistrict();
        MPoint thisBus = thisDist.getBusStop();
        if (player.getPoint().distanceSquared(thisBus) > busDist * busDist) {
            return player.getLocale().localize(
                    "command.district.bus-max-distance", busDist);
        }

        districtName = districtName.toUpperCase().trim();
        District d = city.getDistrictByName(districtName);
        if (d == null) {
            return player.getLocale().localize("command.district.not-exist",
                    districtName);
        }

        MPoint bus = d.getBusStop();
        if (bus == null) {
            return player.getLocale().localize("command.district.no-bus");
        }

        player.teleportWithCountdown(bus);
        return null;
    }

    public String doDeposit(MPlayer player, String amount) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        double amt;
        try {
            amt = Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            return player.getLocale().localize("command.general.invalid-number",
                    amount);
        }

        String amtFmt = StringUtils.formatCurrency(amt);
        if (!player.hasEnough(amt)) {
            return player.getLocale().localize(
                    "command.general.no-money.deposit", amtFmt, StringUtils.
                    formatCurrency(player.getMoney()));
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        player.transferMoney(city, amt);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.deposited", amtFmt, city.getOwnerName()));
        return null;
    }

    public String doWithdraw(MPlayer player, String amount) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        double amt;
        try {
            amt = Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            return player.getLocale().localize("command.general.invalid-number");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-mayor.withdraw");
        }

        String amtFmt = StringUtils.formatCurrency(amt);
        if (!city.hasEnough(amt)) {
            return player.getLocale().localize("command.city.no-money.withdraw",
                    StringUtils.formatCurrency(city.getMoney()), amtFmt);
        }

        city.transferMoney(player, amt);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "comand.city.withdraw", amtFmt, city.getOwnerName()));
        return null;
    }

    public String doClaim(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMember(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-member.claim");
        }

        District d = player.getDistrict();
        if (d.getType().isGovernment()) {
            return player.getLocale().localize("command.city.already-owned");
        }

        double amt = Config.getDouble("prices.city.claim");
        String amtFmt = StringUtils.formatCurrency(amt);
        if (!city.hasEnough(amt)) {
            return player.getLocale().localize("command.city.no-money.claim",
                    amtFmt);
        }

        Section section = player.getSection();
        String sn = city.getSectionName(section);

        //Claim the section
        d.setOwner(section, city);

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.claimed", sn));
        return null;
    }

    public String doUnclaim(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMember(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-member.unclaim");
        }

        District d = player.getDistrict();
        if (d.getType().isGovernment()) {
            return player.getLocale().localize("command.city.already-owned");
        }

        Section section = player.getSection();
        String sn = city.getSectionName(section);

        //Unclaim the section
        d.removeOwner(section);

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.unclaimed", sn));
        return null;
    }

    public String doMakePolice(MPlayer player, String chief,
            String assistant) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-mayor.make-police");
        }

        MPlayer c = Mafiacraft.getPlayer(chief);
        if (c == null) {
            return player.getLocale().localize(
                    "command.general.player-not-found", chief);
        }

        MPlayer a = Mafiacraft.getPlayer(assistant);
        if (a == null) {
            return player.getLocale().localize(
                    "command.general.player-not-found", assistant);
        }

        double amt = Config.getDouble("prices.city.makepolice");
        if (!city.hasEnough(amt)) {
            return player.getLocale().localize(
                    "command.city.no-money.make-police", StringUtils.
                    formatCurrency(amt));
        }

        city.establishPolice(c, a);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.police-made"));
        return null;
    }

    public String doSetChief(MPlayer player, String chief) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-mayor.set-chief");
        }

        MPlayer c = Mafiacraft.getPlayer(chief);
        if (c == null) {
            return player.getLocale().localize(
                    "command.general.player-not-found", chief);
        }

        Government police = city.getPolice();
        if (police == null) {
            return player.getLocale().localize("command.city.no-police");
        }

        police.setLeader(c);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.set-chief", city.getOwnerName(), chief));
        return null;
    }

    public String doSetAssistant(MPlayer player, String assistant) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        City city = player.getCity();
        if (city == null) {
            return player.getLocale().localize("command.city.not-in");
        }

        if (!city.isMayor(player)) {
            return player.getLocale().localize(
                    "command.city.must-be-mayor.set-assistant");
        }

        MPlayer a = Mafiacraft.getPlayer(assistant);
        if (a == null) {
            return player.getLocale().localize(
                    "command.general.player-not-found");
        }

        Government police = city.getPolice();
        if (police == null) {
            return player.getLocale().localize("command.city.no-police");
        }

        police.setViceLeader(a);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.city.set-assistant", city.getOwnerName(), assistant));
        return null;
    }

}
