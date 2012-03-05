/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.geo;

import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.MafiacraftPlugin;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.util.GeoUtils;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * Manager for handling district objects.
 */
public class CityManager {
    private Map<World, CityWorld> cityWorldMap = new HashMap<World, CityWorld>();

    private TIntObjectMap<City> cities = new TIntObjectHashMap<City>();

    private Map<String, TIntObjectMap<District>> worlds = new HashMap<String, TIntObjectMap<District>>();

    private Map<String, LandOwner> landOwners = new HashMap<String, LandOwner>();

    private final MafiacraftPlugin mc;

    public CityManager(MafiacraftPlugin mc) {
        this.mc = mc;
        ConfigurationSerialization.registerClass(City.class);
        ConfigurationSerialization.registerClass(District.class);
        ConfigurationSerialization.registerClass(CityWorld.class);
    }

    /////////////////
    // CITY WORLD
    /////////////////
    /**
     * Gets a city world from a world.
     *
     * @param world
     * @return
     */
    public CityWorld getCityWorld(World world) {
        CityWorld cworld = cityWorldMap.get(world);
        if (cworld == null) {
            cworld = new CityWorld(world);
            cityWorldMap.put(world, cworld);
        }
        return cworld;
    }

    /////////////////
    // CITY
    /////////////////
    /**
     * Gets a city from its integer id.
     *
     * @param id
     * @return
     */
    public City getCity(int id) {
        return cities.get(id);
    }

    /**
     * Gets a city from its name.
     *
     * @param name
     * @return
     */
    public City getCity(String name) {
        for (City city : getCityList()) {
            if (city.getName().equalsIgnoreCase(name)) {
                return city;
            }
        }
        return null;
    }

    /**
     * Gets a list of all cities on the server.
     *
     * @return
     */
    public List<City> getCityList() {
        return new ArrayList<City>(cities.valueCollection());
    }

    /**
     * Returns true if a city already exists with the given name.
     *
     * @param name
     * @return
     */
    public boolean cityExists(String name) {
        return getCity(name) != null;
    }

    /**
     * Founds a city.
     *
     * @param player
     * @param name
     * @param center
     * @return
     */
    public City foundCity(MPlayer player, String name, District center) {
        //Make city
        City city = new City(getNextCityId());
        city.setName(name);
        city.setMayor(player.getName());
        city.attachNewDistrict(center);
        center.setType(DistrictType.GOVERNMENT);
        cities.put(city.getId(), city);
        Mafiacraft.getCityManager().getCityWorld(center.getWorld()).setCapital(city);
        return city;
    }

    /**
     * Gets the next available city ID.
     *
     * @return
     */
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
        return getDistrict(player.getBukkitEntity().getLocation().getChunk());
    }

    /**
     * Gets the district that a chunk is part of. This will create a district if
     * it needs to.
     *
     * @param chunk
     * @return
     */
    public District getDistrict(Chunk chunk) {
        int dx = chunk.getX() >> 4;
        int dz = chunk.getZ() >> 4;
        return getDistrict(chunk.getWorld(), dx, dz);
    }

    /**
     * Gets the district in the specified world. This will create a district if
     * it needs to.
     *
     * @param world
     * @param x
     * @param z
     * @return
     */
    public District getDistrict(World world, int x, int z) {
        int id = GeoUtils.coordsToDistrictId(x, z);
        District d = getDistrictMap(world).get(id);
        if (d == null) {
            d = (getDistrictList(world).size() <= 0)
                    ? createDistrict(world, x, z)
                    : createDistrict(world, x, z);
        }
        return d;
    }

    /**
     * Creates a district based on a sample chunk within the potential district.
     *
     * @param sample
     * @return
     */
    private District createDistrict(Chunk sample) {
        return createDistrict(sample.getWorld(), ((sample.getX()) >> 4), ((sample.getZ() >> 4)));
    }

    /**
     * Creates a district for the specified city.
     *
     * @param x
     * @param z
     * @param city
     * @return The created district, or the existing district
     */
    private District createDistrict(World world, int x, int z) {
        District d = new District(world, x, z);
        getDistrictMap(world).put(d.getId(), d);
        MLogger.logVerbose("A district was created in the world '" + world.getName() + "' at (" + x + ", " + z + ").");
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
                City dc = district.getCity();
                if (dc != null && dc.equals(city)) {
                    districts.add(district);
                }
            }
        }
        return districts;
    }

    /**
     * Disbands a city and wipes it off of the map. Forever.
     *
     * @param city
     * @return
     */
    public CityManager disbandCity(City city) {
        for (District district : city.getDistricts()) {
            district.detachFromCity();
        }

        CityWorld cw = city.getCityWorld();
        City capital = cw.getCapital();
        if (capital.equals(city)) {
            cw.removeCapital();
        }
        return this;
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

    /////////////
    // MISC
    /////////////
    /**
     * Gets a LandOwner based on their id.
     *
     * @param id
     * @return
     */
    public LandOwner getLandOwner(String id) {
        return landOwners.get(id);
    }

    /**
     * Registers a landowner with the manager.
     *
     * @param owner
     * @return
     */
    public CityManager registerLandOwner(LandOwner owner) {
        landOwners.put(owner.getOwnerId(), owner);
        return this;
    }

    ///////////
    // SAVING
    ///////////
    public void save() {
    }

    private void saveCities() {
    }

    private File getCityFolder() {
        File folder = new File(mc.getDataFolder().getPath() + File.separator + "city" + File.separator);
        folder.mkdirs();
        return folder;
    }

}
