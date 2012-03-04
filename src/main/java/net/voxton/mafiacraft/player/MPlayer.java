/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.player;

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.chat.ChatType;
import net.voxton.mafiacraft.classes.UtilityClass;
import net.voxton.mafiacraft.gov.Division;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.gov.Position;
import net.voxton.mafiacraft.util.ConfigSerializable;
import net.voxton.mafiacraft.vault.Transactable;
import java.util.ArrayList;
import java.util.List;
import net.milkbowl.vault.economy.EconomyResponse;
import net.voxton.mafiacraft.geo.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class MPlayer extends Transactable implements LandPurchaser, ConfigSerializable {
    private final OfflinePlayer offlinePlayer;

    private Player onlinePlayer = null;

    private String title;

    private SessionStore store;

    private UtilityClass utilityClass;

    private ChatType chatType;

    private int power;

    private int land;

    public MPlayer(OfflinePlayer player) {
        this.offlinePlayer = player;
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
        return Mafiacraft.getVaultHelper().getEconomy().getBalance(offlinePlayer.getName());
    }

    @Override
    public double setMoney(double amt) {
        double deposit = amt - getMoney();
        if (deposit > 0) {
            return Mafiacraft.getVaultHelper().getEconomy().depositPlayer(offlinePlayer.getName(), deposit).balance;
        } else if (deposit < 0) {
            return Mafiacraft.getVaultHelper().getEconomy().withdrawPlayer(offlinePlayer.getName(), -deposit).balance;
        }
        return 0;
    }

    @Override
    public double addMoney(double amount) {
        EconomyResponse response = Mafiacraft.getVaultHelper().getEconomy().depositPlayer(offlinePlayer.getName(), amount);
        return response.balance;
    }

    @Override
    public double subtractMoney(double amount) {
        EconomyResponse response = Mafiacraft.getVaultHelper().getEconomy().withdrawPlayer(offlinePlayer.getName(), amount);
        return response.balance;
    }

    public Player getBukkitEntity() {
        if (onlinePlayer == null) {
            onlinePlayer = offlinePlayer.getPlayer();
            if (onlinePlayer == null) {
                return null;
            }
        }
        return onlinePlayer;
    }

    public boolean isOnline() {
        return getBukkitEntity() != null;
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

    public boolean canBuild(MPlayer player, Chunk chunk) {
        //TODO: check if the player lets the person build in that chunk?
        return player.equals(this);
    }

    public String getName() {
        return offlinePlayer.getName();
    }

    public String getDisplayName() {
        return getBukkitEntity().getDisplayName();
    }

    public String getOwnerName() {
        return offlinePlayer.getName();
    }

    public void sendMessage(String message) {
        getBukkitEntity().sendMessage(message);
    }

    public String getOwnerId() {
        return "P-" + getName();
    }

    /**
     * Gets the chunk the player is currently in.
     *
     * @return
     */
    public Chunk getChunk() {
        return getBukkitEntity().getLocation().getChunk();
    }

    /**
     * Gets the district the player is currently in.
     *
     * @return
     */
    public District getDistrict() {
        return Mafiacraft.getCityManager().getDistrict(getChunk());
    }

    /**
     * {@inheritDoc}
     */
    public boolean canBeClaimed(Chunk chunk, LandOwner futureOwner) {
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
    public MPlayer claim(Chunk chunk) {
        District district = Mafiacraft.getDistrict(chunk);
        district.setOwner(chunk, this);
        incLand();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public MPlayer unclaim(Chunk chunk) {
        District district = Mafiacraft.getDistrict(chunk);
        district.setOwner(chunk, null);
        decLand();
        return this;
    }

    public Location getLocation() {
        return getBukkitEntity().getLocation();
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

    public MPlayer load(ConfigurationSection source) {
        chatType = ChatType.valueOf(source.getString("chattype", ChatType.DEFAULT.getName()));
        land = source.getInt("land", 0);
        power = source.getInt("power", 0);
        title = source.getString("title", "");
        utilityClass = UtilityClass.valueOf(source.getString("clazz.utility", UtilityClass.NONE.toString()));

        return this;
    }

    public MPlayer save(ConfigurationSection dest) {
        dest.set("chattype", chatType.getName());
        dest.set("land", land);
        dest.set("power", power);
        dest.set("title", (title == null) ? "" : title);
        dest.set("clazz.utility", (utilityClass == null) ? "" : utilityClass.toString());

        return this;
    }

    /**
     * Gets the CityWorld the player is in.
     *
     * @return
     */
    public CityWorld getCityWorld() {
        return Mafiacraft.getCityManager().getCityWorld(getLocation().getWorld());
    }

    /**
     * Checks if the MPlayer has permission to do the given thing.
     *
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        return getBukkitEntity().hasPermission(permission);
    }

    public String getEntryMessage() {
        return getName();
    }

}
