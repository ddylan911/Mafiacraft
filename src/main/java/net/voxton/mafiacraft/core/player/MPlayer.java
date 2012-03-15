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
package net.voxton.mafiacraft.core.player;

import net.voxton.mafiacraft.core.city.LandPurchaser;
import net.voxton.mafiacraft.core.city.OwnerType;
import net.voxton.mafiacraft.core.city.LandOwner;
import net.voxton.mafiacraft.core.city.District;
import net.voxton.mafiacraft.core.city.City;
import net.voxton.mafiacraft.core.chat.ChatType;
import net.voxton.mafiacraft.core.classes.UtilityClass;
import net.voxton.mafiacraft.core.gov.Division;
import net.voxton.mafiacraft.core.gov.Government;
import net.voxton.mafiacraft.core.gov.Position;
import net.voxton.mafiacraft.core.econ.Transactable;
import java.util.ArrayList;
import java.util.List;
import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.action.ActionPerformer;
import net.voxton.mafiacraft.core.geo.MPoint;
import net.voxton.mafiacraft.core.geo.MWorld;
import net.voxton.mafiacraft.core.geo.Section;
import net.voxton.mafiacraft.core.locale.Locale;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents a player.
 */
public class MPlayer extends Transactable implements LandPurchaser, ActionPerformer {

    private final String name;

    private String displayName;

    private String title;

    private SessionStore store;

    private UtilityClass utilityClass;

    private ChatType chatType;

    private int power;

    private int land;

    public MPlayer(String name) {
        this.name = name;
    }

    /**
     * Gets the player's power.
     *
     * @return
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets power of the player.
     *
     * @param power
     * @return
     */
    public MPlayer setPower(int power) {
        this.power = power;
        return this;
    }

    /**
     * Adds power to the player.
     *
     * @param power
     * @return
     */
    public MPlayer addPower(int power) {
        return setPower(getPower() + power);
    }

    /**
     * Tries to add as much of the power as possible to the player.
     *
     * @param power
     * @return
     */
    public MPlayer tryToAddPower(int power) {
        if (!canGainPower(power)) {
            return setPower(getMaxPower());
        }
        return addPower(power);
    }

    /**
     * Subtracts power from the player.
     *
     * @param power
     * @return
     */
    public MPlayer subtractPower(int power) {
        return setPower(getPower() - power);
    }

    /**
     * Tries to subtract as much of the power as possible from the player.
     *
     * @param power
     * @return
     */
    public MPlayer tryToSubtractPower(int power) {
        if (!canLosePower(power)) {
            return setPower(getMinPower());
        }
        return subtractPower(power);
    }

    /**
     * Returns true if a player can gain all of the specified power.
     *
     * @param amount
     * @return
     */
    public boolean canGainPower(int amount) {
        return canHavePower(getPower() + amount);
    }

    /**
     * Returns true if a player can lose all of the specified power.
     *
     * @param amount
     * @return
     */
    public boolean canLosePower(int amount) {
        return canHavePower(getPower() - amount);
    }

    /**
     * Returns true if it is possible for the player to follow the laws of the
     * game with the given power level.
     *
     * @param potential
     * @return
     */
    public boolean canHavePower(int potential) {
        return (potential <= getMaxPower()) && (potential >= getMinPower());
    }

    /**
     * Gets the maximum amount of power the player can have.
     *
     * @return
     */
    public int getMaxPower() {
        switch (getPosition()) {
            default:
            case NONE:
            case AFFILIATE:
                return 0;

            case WORKER:
            case MANAGER:
                return getDivision().getMaxPlayerPower();

            case OFFICER:
            case VICE_LEADER:
            case LEADER:
                return getGovernment().getMaxPlayerPower();
        }
    }

    /**
     * Gets the minimum amount of power the player can have.
     *
     * @return
     */
    public int getMinPower() {
        switch (getPosition()) {
            default:
            case NONE:
            case AFFILIATE:
                return 0;

            case WORKER:
            case MANAGER:
                return getDivision().getMinPlayerPower();

            case OFFICER:
            case VICE_LEADER:
            case LEADER:
                return getGovernment().getMinPlayerPower();
        }
    }

    /**
     * Resets a player's power.
     *
     * @return
     */
    public MPlayer resetPower() {
        return setPower(0);
    }

    @Override
    public double getMoney() {
        return Mafiacraft.getImpl().getMoney(this);
    }

    @Override
    public double setMoney(double amt) {
        return Mafiacraft.getImpl().setMoney(this, amt);
    }

    @Override
    public double addMoney(double amount) {
        return setMoney(getMoney() + amount);
    }

    @Override
    public double subtractMoney(double amount) {
        return setMoney(getMoney() - amount);
    }

    public boolean isOnline() {
        return Mafiacraft.getImpl().isOnline(this);
    }

    /**
     * Gets the player's title.
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the player's title.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the player's current chatType.
     *
     * @return
     */
    public ChatType getChatType() {
        if (chatType == null) {
            chatType = ChatType.DEFAULT;
        }
        return chatType;
    }

    public UtilityClass getUtilityClass() {
        return utilityClass;
    }

    public void setUtilityClass(UtilityClass classType) {
        this.utilityClass = classType;
    }

    /**
     * Gets the division the player is part of, if it exists.
     *
     * @return
     */
    public Division getDivision() {
        Government gov = getGovernment();
        if (gov == null) {
            return null;
        }
        return gov.getDivision(this);
    }

    /**
     * Gets the city the player is standing in.
     *
     * @return
     */
    public City getCity() {
        return getDistrict().getCity();
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public Government getGovernment() {
        return Mafiacraft.getGovernmentManager().getGovernment(this);
    }

    public Position getPosition() {
        Government gov = getGovernment();
        if (gov == null) {
            return Position.NONE;
        }
        return gov.getPosition(this);
    }

    public SessionStore getSessionStore() {
        if (store == null) {
            store = new SessionStore();
        }
        return store;
    }

    /**
     * Clears the player's session store.
     *
     * @return This MPlayer.
     */
    public MPlayer clearSessionStore() {
        store = null;
        return this;
    }

    public boolean canBuild(MPlayer player, Section section) {
        //TODO: check if the player lets the person build in that chunk?
        return player.equals(this);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public MPlayer setDisplayName(String displayName) {
        this.displayName = displayName;
        //TODO: update in the impl
        return this;
    }

    public String getOwnerName() {
        return getName();
    }

    public void sendMessage(String message) {
        Mafiacraft.getImpl().sendMessage(this, message);
    }

    public String getOwnerId() {
        return "P" + getName();
    }

    /**
     * Gets the chunk the player is currently in.
     *
     * @return
     */
    public Section getSection() {
        return getPoint().getSection();
    }

    /**
     * Gets the district the player is currently in.
     *
     * @return
     */
    public District getDistrict() {
        return getSection().getDistrict();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canBeClaimed(Section section, LandOwner futureOwner) {
        return false;
    }

    /**
     * Gets the kill score of this player.
     *
     * @return
     */
    public int getKillScore() {
        return Mafiacraft.getPlayerManager().getKillTracker().getKillScore(this);
    }

    public OwnerType getOwnerType() {
        return OwnerType.PLAYER;
    }

    /**
     * {@inheritDoc}
     */
    public int getLand() {
        return land;
    }

    /**
     * {@inheritDoc}
     */
    public MPlayer setLand(int amt) {
        this.land = amt;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public MPlayer incLand() {
        land++;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public MPlayer decLand() {
        land--;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MPlayer claim(Section section) {
        section.getDistrict().setOwner(section, this);
        incLand();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MPlayer unclaim(Section section) {
        District district = section.getDistrict();
        district.setOwner(section, null);
        decLand();
        return this;
    }

    /**
     * Gets the point where the player is located.
     *
     * @return The point where the player is located.
     */
    public MPoint getPoint() {
        return Mafiacraft.getImpl().getPoint(this);
    }

    /**
     * Gets the city the player owns. Only works if the player is a mayor over a
     * city.
     *
     * @return
     */
    public List<City> getOwnedCities() {
        List<City> cities = new ArrayList<City>();
        for (City city : Mafiacraft.getCityManager().getCityList()) {
            if (city.isMayor(this)) {
                cities.add(city);
            }
        }
        return cities;
    }

    /**
     * Returns true if the given player is a mayor of a city.
     *
     * @return
     */
    public boolean isAMayor() {
        return getOwnedCities().size() > 0;
    }

    /**
     * Loads this MPlayer from the given ConfigurationSection.
     *
     * @param source The ConfigurationSection to load from.
     * @return This MPlayer, loaded and READY TO RUMBBBLEEE
     */
    public MPlayer load(ConfigurationSection source) {
        chatType = ChatType.valueOf(source.getString("chattype",
                ChatType.DEFAULT.getName()));
        land = source.getInt("land", 0);
        power = source.getInt("power", 0);
        title = source.getString("title", "");
        utilityClass = UtilityClass.valueOf(source.getString("clazz.utility",
                UtilityClass.NONE.toString()));

        return this;
    }

    /**
     * Saves the MPlayer to the given ConfigurationSection.
     *
     * @param dest The destination to save to.
     * @return This MPlayer, persisted.
     */
    public MPlayer save(ConfigurationSection dest) {
        dest.set("chattype", chatType.getName());
        dest.set("land", land);
        dest.set("power", power);
        dest.set("title", (title == null) ? "" : title);
        dest.set("clazz.utility", (utilityClass == null) ? "" : utilityClass.
                name());

        return this;
    }

    /**
     * Gets the CityWorld the player is in.
     *
     * @return
     */
    public MWorld getWorld() {
        return getPoint().getWorld();
    }

    /**
     * Checks if the MPlayer has permission to do the given thing.
     *
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        return Mafiacraft.getImpl().hasPermission(this, permission);
    }

    /**
     * {@inheritDoc}
     */
    public String getEntryMessage() {
        return getName();
    }

    /**
     * Gets the locale of the player. At the moment this is only English US.
     *
     * @return The locale of the player.
     */
    public Locale getLocale() {
        return Mafiacraft.getDefaultLocale();
    }

    /**
     * Teleports the player to the given point.
     *
     * @param point The point to teleport to.
     * @return This MPlayer.
     */
    public MPlayer teleport(MPoint point) {
        Mafiacraft.getImpl().teleportPlayer(this, point);
        return this;
    }

    /**
     * Teleports the player to a given location in the default time.
     *
     * @param point The point to teleport to.
     * @return This MPlayer.
     */
    public MPlayer teleportWithCountdown(MPoint point) {
        return teleportWithCountdown(point, 10);
    }

    /**
     * Teleports the player to a given location in the given time.
     *
     * @param point The point to teleport to.
     * @param duration The duration of teleportation in seconds.
     * @return This MPlayer.
     */
    public MPlayer teleportWithCountdown(MPoint point, int duration) {
        Mafiacraft.getPlayerManager().startTeleport(this, duration, point);
        return this;
    }

}
