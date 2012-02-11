/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.gov.LandOwner;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.util.GeoUtils;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * Manager for handling district objects.
 */
public class CityManager {
    private TIntObjectMap<City> cities = new TIntObjectHashMap<City>();

    private Map<String, TIntObjectMap<District>> worlds = new HashMap<String, TIntObjectMap<District>>();

    private final Mafiacraft mc;

    public CityManager(Mafiacraft mc) {
        this.mc = mc;
    }

    /////////////////
    // CITY
    /////////////////
    public City getCity(int id) {
        return cities.get(id);
    }

    public City getCity(String name) {
        for (City city : getCityList()) {
            if (city.getName().equalsIgnoreCase(name)) {
                return city;
            }
        }
        return null;
    }

    public List<City> getCityList() {
        return new ArrayList<City>(cities.valueCollection());
    }

    public boolean cityExists(String name) {
        return getCity(name) != null;
    }

    public City foundCity(String name, District center) {
        //Make city
        City city = new City(getNextCityId());
        city.setName(name);
        center.setCity(null).setName(city.getNextDistrictName());
        cities.put(city.getId(), city);

        //Make government
        Government government = mc.getGovernmentManager().createGovernment(name);
        mc.getGovernmentManager().setCityGovernment(city, government);
        
        return city;
    }

    public int getNextCityId() {
        int id = 0;
        for (int i = 1; getCity(i) != null; ++i) {
            id = i;
        }
        return id;
    }

    /////////////////
    // DISTRICT METHODS
    /////////////////
    /**
     * Gets the district a player is in.
     * 
     * @param player
     * @return 
     */
    public District getDistrict(MPlayer player) {
        return getDistrict(player.getPlayer().getLocation().getChunk());
    }

    /**
     * Gets the district that a chunk is part of.
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

    public District createDistrict(Chunk sample) {
        return createDistrict(sample.getWorld(), ((sample.getX()) >> 4), ((sample.getZ() >> 4)));
    }

    /**
     * Creates a district for the specified city.
     * 
     * @param x
     * @param z
     * @param city
     * @return 
     */
    public District createDistrict(World world, int x, int z) {
        District d = new District(world, x, z);
        getDistrictMap(world).put(d.getId(), d);
        return d;
    }

    /**
     * Gets the map of districts to coordinates in a world.
     * 
     * @param world
     * @return 
     */
    private TIntObjectMap<District> getDistrictMap(World world) {
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
        for (World world : Bukkit.getWorlds()) {
            for (District district : getDistrictList(world)) {
                if (district.getCity().equals(city)) {
                    districts.add(district);
                }
            }
        }
        return districts;
    }

    /////////////////
    // SECTION METHODS
    /////////////////
    /**
     * Gets the owner of a section.
     * 
     * @param chunk
     * @return 
     */
    public LandOwner getSectionOwner(Chunk chunk) {
        int x = chunk.getX() % 0x10;
        int z = chunk.getZ() % 0x10;
        return getDistrict(chunk).getOwner(x, z);
    }

    /**
     * Gets the name of the specified section.
     * 
     * @param chunk
     * @return 
     */
    public String getSectionName(Chunk chunk) {
        District d = getDistrict(chunk);
        StringBuilder nameBuilder = new StringBuilder(d.getName()).append('-');
        byte sid = d.getSectionId(chunk);
        nameBuilder.append(Byte.toString(sid));
        return nameBuilder.toString();
    }

    public void claimSection(Chunk chunk, LandOwner owner) {
    }

}
