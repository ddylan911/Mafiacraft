/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.util.GeoUtils;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * Manager for handling district objects.
 */
public class DistrictManager {
    private Map<String, TIntObjectMap<District>> worlds = new HashMap<String, TIntObjectMap<District>>();
    
    /**
     * Gets the government that owns a chunk.
     * 
     * @param chunk
     * @return 
     */
    public Government getOwner(Chunk chunk) {
        int x = chunk.getX() % 0x10;
        int z = chunk.getZ() % 0x10;
        return getDistrict(chunk).getOwner(x, z);
    }

    /**
     * Gets the district that owns a chunk.
     * 
     * @param chunk
     * @return 
     */
    public District getDistrict(Chunk chunk) {
        int dx = chunk.getX() >> 4;
        int dz = chunk.getZ() >> 4;
        int id = GeoUtils.coordsToDistrictId(dx, dz);
        return getDistrictMap(chunk.getWorld()).get(id);
    }
    
    /**
     * Creates a district for the specified city.
     * 
     * @param x
     * @param z
     * @param city
     * @return 
     */
    public District createDistrict(int x, int z, City city) {
        int id = GeoUtils.coordsToDistrictId(x, z);
        District d = new District(id, x, z, city);
        getDistrictMap(city.getWorld()).put(id, d);
        return d;
    }

    /**
     * Gets the map of districts to coordinates in a world.
     * 
     * @param world
     * @return 
     */
    public TIntObjectMap<District> getDistrictMap(World world) {
        TIntObjectMap<District> worldDistricts = worlds.get(world.getName());
        if (worldDistricts == null) {
            worldDistricts = new TIntObjectHashMap<District>();
            worlds.put(world.getName(), worldDistricts);
        }
        return worldDistricts;
    }

    /**
     * Gets a list of all districts in a world.
     * 
     * @param world
     * @return 
     */
    public List<District> getDistrictList(World world) {
        return new ArrayList<District>(getDistrictMap(world).valueCollection());
    }

    /**
     * Gets a list of all districts in a city.
     * 
     * @param city
     * @return 
     */
    public List<District> getCityDistricts(City city) {
        List<District> districts = new ArrayList<District>();
        for (District district : getDistrictList(city.getWorld())) {
            if (district.getCity().equals(city)) {
                districts.add(district);
            }
        }
        return districts;
    }

}
