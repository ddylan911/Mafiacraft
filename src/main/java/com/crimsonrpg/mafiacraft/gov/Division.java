/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.geo.LandOwner;
import com.crimsonrpg.mafiacraft.MConfig;
import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.geo.LandPurchaser;
import com.crimsonrpg.mafiacraft.geo.OwnerType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.vault.Transactable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author simplyianm
 */
public class Division extends Transactable implements LandPurchaser {

    private int id;
    private Government government;
    private String name;
    private String description;
    private String manager;
    private String prefix;
    private List<String> workers = new ArrayList<String>();
    private int land;

    public Division(int id, Government government, String prefix) {
        this.id = id;
        this.prefix = prefix;
        this.government = government;
    }

    public boolean canBuild(MPlayer player, Chunk chunk) {
        return true;
    }

    public void decrementLand() {
        land--;
    }

    public void incrementLand() {
        land++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;

    }

    public String getOwnerName() {
        return name + " of " + government.getName();
    }

    public String getOwnerId() {
        return "D-" + government.getId() + "-" + id;
    }

    public Government getGovernment() {
        return government;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Division addWorker(String player) {
        workers.add(player);
        return this;
    }

    public Division addWorker(MPlayer player) {
        return addWorker(player.getName());
    }

    public Position getPosition(String player) {
        if (isWorker(player)) {
            return Position.WORKER;
        }

        if (isManager(player)) {
            return Position.MANAGER;
        }

        return Position.NONE;
    }

    /**
     * Removes a player from the division.
     * 
     * @param player
     * @return True if the operation was allowed.
     */
    public boolean remove(String player) {
        switch (getPosition(player)) {
            case WORKER:
                workers.remove(player);
                break;

            case MANAGER:
                return false;
        }
        return true;
    }

    public List<String> getWorkers() {
        return new ArrayList<String>(workers);
    }

    public boolean isWorker(String player) {
        return workers.contains(player);
    }

    public boolean isWorker(MPlayer player) {
        return isWorker(player.getName());
    }

    public boolean isManager(String player) {
        return manager.equals(player);
    }

    public boolean isManager(MPlayer player) {
        return isManager(player.getName());
    }

    public boolean isMember(String player) {
        return isWorker(player) || isManager(player);
    }

    /**
     * Loads the division from a ConfigurationSection.
     * 
     * @param source
     * @return 
     */
    public Division load(ConfigurationSection source) {
        name = source.getString("name", "null");
        description = source.getString("desc", "Default division description...");
        manager = source.getString("members.manager");
        workers = source.getStringList("members.workers");
        return this;
    }

    /**
     * Saves the division to a ConfigurationSection.
     * 
     * @param dest
     * @return 
     */
    public Division save(ConfigurationSection dest) {
        dest.set("name", name);
        dest.set("desc", description);
        dest.set("members.manager", manager);
        dest.set("members.workers", workers);
        return this;
    }

    /**
     * Gets a list of all members of the division.
     * 
     * @return 
     */
    public List<String> getMembers() {
        List<String> members = getWorkers();
        members.add(manager);
        return members;
    }

    /**
     * Gets a list of all members currently online in the division.
     * 
     * @return 
     */
    public List<MPlayer> getOnlineMembers() {
        List<MPlayer> online = new ArrayList<MPlayer>();
        List<String> members = getMembers();
        for (MPlayer player : Mafiacraft.getOnlinePlayers()) {
            if (members.contains(player.getName())) {
                online.add(player);
            }
        }
        return online;
    }

    public static String validateName(String name) {
        int max = MConfig.getInt("division.maxnamelength");
        if (name.length() < max) {
            return "Name must be under " + max + " characters.";
        }

        if (!name.matches("[A-Za-z0-9]+")) {
            return "Name must be alphanumeric.";
        }

        return null;
    }

    /**
     * Gets the maximum amount of land this division can own.
     * This is determined by the money the division has.
     * 
     * @return 
     */
    public int getMaxLand() {
        return ((int) getMoney()) >> 4;
    }

    public boolean canBeClaimed(Chunk chunk, LandOwner futureOwner) {
        return government.canRetainAllLand();
    }

    public OwnerType getOwnerType() {
        return OwnerType.DIVISION;
    }
    ////////////
    // LAND STUFF
    ////////////

    /**
     * {@inheritDoc}
     */
    public int getLand() {
        return land;
    }

    /**
     * {@inheritDoc}
     */
    public Division setLand(int amt) {
        this.land = amt;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Division incLand() {
        land++;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Division decLand() {
        land--;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Division claim(Chunk chunk) {
        District district = Mafiacraft.getDistrict(chunk);
        district.setOwner(chunk, this);
        incLand();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Division unclaim(Chunk chunk) {
        District district = Mafiacraft.getDistrict(chunk);
        district.setOwner(chunk, null);
        decLand();
        return this;
    }
}
