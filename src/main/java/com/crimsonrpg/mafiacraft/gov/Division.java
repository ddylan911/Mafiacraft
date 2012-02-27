/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.geo.LandOwner;
import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.geo.LandPurchaser;
import com.crimsonrpg.mafiacraft.geo.OwnerType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.util.ConfigSerializable;
import com.crimsonrpg.mafiacraft.vault.Transactable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents a government division.
 */
public class Division extends Transactable implements LandPurchaser, ConfigSerializable {
    private int id;

    private Government government;

    private String name;

    private String description;

    private String manager;

    private List<String> workers = new ArrayList<String>();

    private int land;

    private Location hq;

    public Division(int id, Government government) {
        this.id = id;
        this.government = government;
    }

    /**
     * Gets the HQ location.
     *
     * @return
     */
    public Location getHq() {
        return hq;
    }

    /**
     * Sets the HQ to the given location.
     *
     * @param hq
     */
    public void setHq(Location hq) {
        this.hq = hq;
    }

    /**
     * Gets the section in which the HQ is located.
     *
     * @return
     */
    public Chunk getHqSection() {
        if (hq == null) {
            return null;
        }

        return hq.getChunk();
    }

    public boolean canBuild(MPlayer player, Chunk chunk) {
        return getOnlineMembers().contains(player);
    }

    public String getName() {
        return name;
    }

    public Division setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrefix() {
        return name;
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

    public Division setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getManager() {
        return manager;
    }

    public Division setManager(String manager) {
        this.manager = manager;
        return this;
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
     * Gets the amount of members in the divison.
     *
     * @return
     */
    private int getMemberCount() {
        return getMembers().size();
    }

    /**
     * Gets all division members as MPlayers. Potentially expensive!
     *
     * @return
     */
    public List<MPlayer> getMembersAsMPlayers() {
        List<MPlayer> members = new ArrayList<MPlayer>();
        for (String player : getMembers()) {
            MPlayer mp = Mafiacraft.getPlayer(player);
            members.add(mp);
        }
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

    /**
     * Gets the maximum amount of land this division can own. This is determined
     * by the money the division has. This is also known as the "L" variable as
     * described in the Google doc:
     *
     * <p>L = the maximum amount of land the regime can own. (money / 1024)</p>
     *
     * @return
     */
    public int getMaxLand() {
        return ((int) getMoney()) >> 10;
    }

    /**
     * Gets the "P" variable as described in the Google doc:
     *
     * <p>P = (L / players)</p>
     *
     * @return
     */
    public int getPlayerPower() {
        return getMaxLand() / getMemberCount();
    }

    /**
     * Gets the maximum amount of player power for this division. Each player
     * has a "power" between -2P and 2P.
     *
     * @return
     */
    public int getMaxPlayerPower() {
        return getPlayerPower() << 1;
    }

    /**
     * Gets the minimum amount of player power for this division.
     *
     * @return
     */
    public int getMinPlayerPower() {
        return -getMaxPlayerPower();
    }

    /**
     * Gets the combined power of all players in the division.
     *
     * @return
     */
    public int getPower() {
        int power = 0;
        for (MPlayer player : getMembersAsMPlayers()) {
            power += player.getPower();
        }

        return power;
    }

    public boolean canBeClaimed(Chunk chunk, LandOwner futureOwner) {
        return getPower() >= getLand();
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
        district.removeOwner(chunk);
        decLand();
        return this;
    }

}
