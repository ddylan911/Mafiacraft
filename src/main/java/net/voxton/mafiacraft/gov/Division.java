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
package net.voxton.mafiacraft.gov;

import java.util.*;
import java.util.logging.Level;
import net.voxton.mafiacraft.logging.MLogger;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.geo.LandPurchaser;
import net.voxton.mafiacraft.geo.OwnerType;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.vault.Transactable;
import net.voxton.mafiacraft.geo.LandOwner;
import net.voxton.mafiacraft.util.LocationSerializer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a government division.
 */
public class Division extends Transactable implements LandPurchaser, ConfigurationSerializable {

    private int id;

    private String name;

    private String description;

    private String manager;

    private Set<String> workers = new HashSet<String>();

    private int land;

    private Location hq;

    public Division(int id) {
        this.id = id;
    }

    /**
     * Gets the unique ID of this division.
     *
     * @return
     */
    public int getId() {
        return id;
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
    public Division setHq(Location hq) {
        this.hq = hq;
        return this;
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
        return name + " of " + getGovernment().getName();
    }

    public String getOwnerId() {
        return "D" + id;
    }

    /**
     * Gets the government corresponding with this division.
     *
     * @return The government this division is part of.
     */
    public Government getGovernment() {
        return Mafiacraft.getGovernmentManager().getGovernmentOf(this);
    }

    public String getDescription() {
        if (description == null) {
            description = "A division of " + getGovernment().getName();
        }
        return description;
    }

    public Division setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getManager() {
        return manager;
    }

    /**
     * Sets the manager of the division to the player specified.
     *
     * @param manager
     * @return
     */
    public Division setManager(String manager) {
        this.manager = manager;
        return this;
    }

    /**
     * Sets the manager of the division to the player specified.
     *
     * @param manager
     * @return
     */
    public Division setManager(MPlayer manager) {
        return setManager(manager.getName());
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

    /**
     * Gets a list of all workers within the division.
     *
     * @return A list of all division workers.
     */
    public List<String> getWorkers() {
        return new ArrayList<String>(workers);
    }

    /**
     * Sets the workers of the division.
     *
     * @param workers The workers of the division.
     */
    private void setWorkers(Set<String> workers) {
        this.workers = workers;
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
    public int getMemberCount() {
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
     * Returns true if the division's HQ chunk can be claimed.
     *
     * @return
     */
    public boolean canHQBeClaimed() {
        return getLand() <= 1;
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
        if (chunk.equals(getHqSection())) {
            return canHQBeClaimed();
        }
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

    public String getEntryMessage() {
        return getOwnerName() + " - " + getDescription();
    }

    ////////////
    // SERIALIZATION
    ////////////
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id", getId());
        data.put("name", getName());
        data.put("desc", getDescription());
        data.put("land", getLand());

        data.put("manager", getManager());
        data.put("workers", getWorkers());
        data.put("hq", LocationSerializer.serializeFull(getHq()));

        return data;
    }

    /**
     * Deserializes a Division.
     *
     * @param data The data in Map form.
     * @return The deserialized Division.
     */
    public static Division deserialize(Map<String, Object> data) {
        int id = 0;
        String strId = data.get("id").toString();
        try {
            id = Integer.parseInt(strId);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE,
                    "Invalid number encountered when deserializing a government!",
                    ex);
        }

        Division div = new Division(id);

        String name = data.get("name").toString();
        String desc = data.get("desc").toString();

        Map<String, Object> hqM = (Map<String, Object>) data.get("hq");
        Location hq = LocationSerializer.deserializeFull(hqM);

        String landS = data.get("land").toString();
        int land = 0;
        try {
            land = Integer.parseInt(landS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid land amount encountered when loading a district: '"
                    + landS + "'!");
        }

        String manager = data.get("manager").toString();

        List<String> workerList = (List<String>) data.get("workers");
        Set<String> workers = new HashSet<String>(workerList);

        //Insert info
        div.setName(name).setDescription(desc);
        div.setHq(hq).setLand(land);

        //Insert members
        div.setManager(manager).setWorkers(workers);

        return div;
    }

}
