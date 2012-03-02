/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import com.crimsonrpg.mafiacraft.MLogger;
import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.gov.Division;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.util.GeoUtils;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents a 8x8 section area.
 */
public class District implements LandOwner {
    private final int id;

    private String name;

    private final World world;

    private final int x;

    private final int z;

    private Location busStop;

    private City city;

    private DistrictType type;

    private String description;

    private TByteObjectMap<String> owners = new TByteObjectHashMap<String>();

    private double landCost;

    public District(World world, int x, int z) {
        this.id = GeoUtils.coordsToDistrictId(x, z);
        this.world = world;
        this.x = x;
        this.z = z;
    }

    /**
     * Gets the id of the district.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the district
     *
     * @return The name of the district
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the district
     *
     * @param name The name to set
     * @return The district that the name was changed of
     */
    public District setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the world this district is in.
     *
     * @return
     */
    public World getWorld() {
        return world;
    }

    /**
     * {@inheritDoc}
     *
     * @param chunk
     * @return
     */
    public boolean canBeClaimed(Chunk chunk, LandOwner owner) {
        LandOwner currentOwner = getOwner(chunk);
        if (currentOwner.equals(this) && getType().isClaim()) {
            return true;
        }
        if (currentOwner instanceof Division) {
            Division div = (Division) currentOwner;
            if (div.getGovernment().getPower() < div.getGovernment().getLand()) {
                return true;
            }

        }
        return false;
    }

    /**
     * Gets the X coordinate of this district.
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Z coordinate of this district.
     *
     * @return
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the city this district is part of.
     *
     * @return
     */
    public City getCity() {
        return city;
    }

    /**
     * Sets the city of this district.
     *
     * @param city
     * @return
     */
    public District setCity(City city) {
        this.city = city;
        return this;
    }

    /**
     * Gets the type of district this city is.
     *
     * @return
     */
    public DistrictType getType() {
        return type;
    }

    /**
     * Sets the type of district this district is.
     *
     * @param type
     * @return
     */
    public District setType(DistrictType type) {
        this.type = type;
        return this;
    }

    /**
     * Gets the description of the district.
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the district.
     *
     * @param description
     * @return
     */
    public District setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets the owner of a chunk.
     *
     * @param chunk
     * @return The government assigned to the chunk, or null if the chunk is not
     * part of the district.
     */
    public LandOwner getOwner(Chunk chunk) {
        if (!contains(chunk)) {
            throw new IllegalArgumentException("Chunk out of bounds of district " + getName() + "!");
        }
        return getOwner(chunk.getX() % 0x10, chunk.getZ() % 0x10);
    }

    /**
     * Gets the owner of the specified section.
     *
     * @param x
     * @param z
     * @return
     */
    public LandOwner getOwner(int x, int z) {
        byte id = GeoUtils.coordsToSectionId(x, z);
        String ownerStr = owners.get(id);
        LandOwner owner = Mafiacraft.getLandOwner(ownerStr);
        if (owner == null) {
            owner = this;
        }
        return owner;
    }

    /**
     * Sets the owner of a section.
     *
     * @param chunk
     * @param owner
     * @return
     */
    public District setOwner(Chunk chunk, LandOwner owner) {
        if (!contains(chunk)) {
            throw new IllegalArgumentException("Chunk out of bounds of district " + getName() + "!");
        }
        return setOwner(chunk.getX() % 0x10, chunk.getZ() % 0x10, owner);
    }

    /**
     * Sets the owner of a section.
     *
     * @param x
     * @param z
     * @param owner
     * @return
     */
    public District setOwner(int x, int z, LandOwner owner) {
        byte id = GeoUtils.coordsToSectionId(x, z);
        owners.put(id, owner.getOwnerId());
        return this;
    }

    /**
     * Removes the owner of a section.
     *
     * @param chunk
     * @return
     */
    public District removeOwner(Chunk chunk) {
        if (!contains(chunk)) {
            throw new IllegalArgumentException("Chunk out of bounds of district " + getName() + "!");
        }
        return removeOwner(chunk.getX() % 0x10, chunk.getZ() % 0x10);
    }

    /**
     * Removes an owner of a section.
     *
     * @param x
     * @param z
     * @return
     */
    public District removeOwner(int x, int z) {
        byte id = GeoUtils.coordsToSectionId(x, z);
        owners.remove(id);
        return this;
    }

    /**
     * Resets all ownerships in the district.
     *
     * @return
     */
    public District resetOwnerships() {
        owners = new TByteObjectHashMap<String>();
        return this;
    }

    /**
     * Gets the user-friendly name of the section.
     *
     * @param chunk
     * @return
     */
    public String getSectionName(Chunk chunk) {
        short idUnsigned = (short) (getSectionId(chunk) + 127);
        return getName() + '-' + idUnsigned;
    }

    /**
     * Gets the id of the specified section.
     *
     * @param chunk
     * @return
     */
    public byte getSectionId(Chunk chunk) {
        if (!contains(chunk)) {
            return -1;
        }
        return GeoUtils.coordsToSectionId(chunk.getX(), chunk.getZ());
    }

    /**
     * Checks if the district contains the specified location.
     *
     * @param location
     * @return
     */
    public boolean contains(Location location) {
        return contains(location.getChunk());
    }

    /**
     * Checks if the district contains the specified chunk.
     *
     * @param c
     * @return
     */
    public boolean contains(Chunk c) {
        int sx = x << 4;
        int sz = z << 4;
        MLogger.logVerbose("Checking if chunk " + c.getX() + ", " + c.getZ() + " is within the bounds of " + sx + ", " + sz + ".", 5);
        return (c.getX() >= sx)
                && (c.getX() < (sx + 0x10))
                && (c.getZ() >= sz)
                && (c.getZ() < (sz + 0x10));
    }

    public boolean canBuild(MPlayer player, Chunk chunk) {
        if (type.canBuild()) {
            return true;
        }

        if (getCity().isMember(player)) {
            return true;
        }

        return false;
    }

    public String getOwnerName() {
        return "District " + name;
    }

    public String getOwnerId() {
        return "R-" + id;
    }

    /**
     * Gets the bus stop of the district.
     *
     * @return
     */
    public Location getBusStop() {
        return busStop;
    }

    /**
     * Sets the bus stop of the district.
     *
     * @param busStop
     */
    public void setBusStop(Location busStop) {
        this.busStop = busStop;
    }

    /**
     * Gets a list of all players in the district.
     *
     * @return
     */
    public List<MPlayer> getPlayers() {
        List<MPlayer> players = new ArrayList<MPlayer>();
        for (MPlayer player : Mafiacraft.getOnlinePlayers()) {
            if (this.contains(player.getChunk())) {
                players.add(player);
            }
        }
        return players;
    }

    public OwnerType getOwnerType() {
        return OwnerType.DISTRICT;
    }

    /**
     * Detaches the district from whatever city it was part of. It completely
     * resets the district, too.
     *
     * @return
     */
    public District detachFromCity() {
        if (city == null) {
            return this;
        }

        reset().setType(DistrictType.ANARCHIC);
        return this;
    }

    /**
     * Completely resets the district to how it was before, other than
     * buildings.
     *
     * @return
     */
    public District reset() {
        setCity(null);
        setName(null);
        setBusStop(null);
        resetOwnerships();
        setType(DistrictType.UNEXPLORED);
        return this;
    }

    /**
     * Gets a chat friendly name of the district.
     *
     * @return
     */
    public String getNameInChat() {
        if (name != null) {
            City c = getCity();
            return "district " + name + ((c == null) ? "" : " of " + c.getOwnerName());
        }
        return "an unexplored district";
    }

    public double getLandCost() {
        return landCost;
    }

    public void setLandCost(double landCost) {
        this.landCost = landCost;
    }

}
