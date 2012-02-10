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
    private City city;
    
    private int id;

    private int x;

    private int z;

    private String name;

    private String description;

    private TByteObjectMap<Government> owners = new TByteObjectHashMap<Government>();

    public District(int id, int x, int z, City city) {
        this.id = id;
        this.x = x;
        this.z = z;
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public District setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public District setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Government getOwner(int x, int z) {
        byte id = GeoUtils.coordsToSectionId(x, z);
        return owners.get(id);
    }
    
    public boolean contains(Location location) {
        Chunk c = location.getChunk();
        int sx = x << 4;
        int sz = z << 4;
        return (c.getX() > sx)
                && (c.getX() < (sx + 0x10))
                && (c.getZ() > sz)
                && (c.getZ() < (sz + 0x10));
    }


}
