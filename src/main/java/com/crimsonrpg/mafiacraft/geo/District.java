/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;

import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.util.GeoUtils;

/**
 * Represents a 8x8 section area.
 */
public class District {
    private int id;

    private String name;

    private int x;

    private int z;

    private City city;

    private DistrictType type;

    private String description;

    private TByteObjectMap<Government> owners = new TByteObjectHashMap<Government>();

    public District(int id, String name, int x, int z, City city) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.z = z;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public District setName(String name) {
        this.name = name;
        return this;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public City getCity() {
        return city;
    }

    public District setCity(City city) {
        this.city = city;
        return this;
    }

    public DistrictType getType() {
        return type;
    }

    public District setType(DistrictType type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public District setDescription(String description) {
        this.description = description;
        return this;
    }

    public Government getOwner(int x, int z) {
        byte id = GeoUtils.coordsToSectionId(x, z);
        return owners.get(id);
    }
    
    public byte getSectionId(Chunk chunk) {
        if (!contains(chunk)) {
            return -1;
        }
        return GeoUtils.coordsToSectionId(chunk.getX(), chunk.getZ());
    }

    /**
     * Gets the owner of a chunk.
     * 
     * @param chunk
     * @return The government assigned to the chunk, or null if the chunk is not part of the district.
     */
    public Government getGovernment(Chunk chunk) {
        if (!contains(chunk)) {
            return null;
        }
        return getOwner(chunk.getX() % 0x10, chunk.getZ() % 0x10);
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
        return (c.getX() > sx)
                && (c.getX() < (sx + 0x10))
                && (c.getZ() > sz)
                && (c.getZ() < (sz + 0x10));
    }

}
