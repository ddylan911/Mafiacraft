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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * Manager for handling district objects.
 */
public class CityManager {

    /**
     * Contains worlds mapped to their respective cityworlds.
     */
    private Map<World, CityWorld> cityWorldMap = new HashMap<World, CityWorld>();

    /**
     * Contains mappings of cities to their ids.
     */
    private TIntObjectMap<City> cities = new TIntObjectHashMap<City>();

    /**
     * Maps districts to worlds.
     */
    private Map<String, TIntObjectMap<District>> worlds =
            new HashMap<String, TIntObjectMap<District>>();

    /**
     * Holds all of the landowners.
     */
    private Map<String, LandOwner> landOwners = new HashMap<String, LandOwner>();

    /**
     * Contains mappings between cities and districts.
     */
    private Map<City, List<District>> cityDistrictMap =
            new HashMap<City, List<District>>();

    /**
     * Contains mappings between districts and cities.
     */
    private Map<District, City> districtCityMap = new HashMap<District, City>();

    private final MafiacraftPlugin mc;

    /**
     * Constructor.
     *
     * @param mc The MafiacraftPlugin plugin.
     */
    public CityManager(MafiacraftPlugin mc) {
        this.mc = mc;

        registerSerializations();
    }

    /**
     * Registers all the serializable classes to serialize them.
     */
    private void registerSerializations() {
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

    /**
     * Gets a list of city worlds currently loaded.
     *
     * @return The city world list.
     */
    public List<CityWorld> getCityWorldList() {
        return new ArrayList<CityWorld>(cityWorldMap.values());
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
        insertCity(city);
        Mafiacraft.getCityManager().getCityWorld(center.getWorld()).setCapital(
                city);
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

    public District getDistrict(World world, int id) {
        District d = getDistrictMap(world).get(id);

        int x = GeoUtils.xFromDistrictId(id);
        int z = GeoUtils.zFromDistrictId(id);

        if (d == null) {
            d = (getDistrictList(world).size() <= 0)
                    ? createDistrict(world, x, z)
                    : createDistrict(world, x, z);
        }
        return d;
    }

    /**
     * Gets the city that corresponds with the given district.
     *
     * @param district The district
     * @return The city of the district.
     */
    public City getCityOf(District district) {
        return districtCityMap.get(district);
    }

    /**
     * Gets a district from their uid.
     *
     * @param uid The uid of the district as defined in District.getUid().
     * @return The District corresponding with the UID.
     */
    public District getDistrictFromUid(String uid) {
        String[] split = uid.split(";");
        if (split.length < 2) {
            MLogger.log(Level.SEVERE, "Invalid District UID encountered: not enough semicolon delimited parts! UID in question: '"
                    + uid + "'.");
        }

        World world = Bukkit.getWorld(split[0]);
        if (world == null) {
            MLogger.log(Level.SEVERE, "Invalid District UID encountered for world: '"
                    + uid + "'!");
        }

        int id = 0;
        try {
            id = Integer.parseInt(split[1]);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid District UID encoutered for world: '"
                    + uid + "'!");
        }

        return getDistrictMap(world).get(id);
    }

    /**
     * Inserts a city.
     *
     * @param city The city to insert.
     * @return This CityManager.
     */
    public CityManager insertCity(City city) {
        cities.put(city.getId(), city);
        registerLandOwner(city);
        return this;
    }

    /**
     * Inserts the given district.
     *
     * @param district The district to insert.
     * @return This CityManager.
     */
    public CityManager insertDistrict(District district) {
        World world = district.getWorld();
        getDistrictMap(world).put(district.getId(), district);
        registerLandOwner(district);
        return this;
    }

    /**
     * Attaches the district to a city.
     *
     * @param district
     * @param city
     * @return
     */
    public CityManager attachDistrict(District district, City city) {
        districtCityMap.put(district, city);
        getActualCityDistricts(city).add(district);
        return this;
    }

    /**
     * Detaches the district from its city.
     *
     * @param district The district to detach from
     * @return This CityManager.
     */
    public CityManager detachDistrict(District district) {
        City city = district.getCity();
        districtCityMap.remove(district);
        if (city != null) {
            getActualCityDistricts(city).remove(district);
        }
        return this;
    }

    /**
     * Creates a district based on a sample chunk within the potential district.
     *
     * @param sample
     * @return
     */
    private District createDistrict(Chunk sample) {
        return createDistrict(sample.getWorld(), ((sample.getX()) >> 4),
                ((sample.getZ() >> 4)));
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
        insertDistrict(d);
        MLogger.logVerbose("A district was created in the world '" + world.
                getName() + "' at (" + x + ", " + z + ").");
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
     * Gets a list of all Districts loaded.
     *
     * @return The list of districts.
     */
    public List<District> getDistrictList() {
        return new ArrayList<District>(districtCityMap.keySet());
    }

    /**
     * Gets the actual city district mapping for the given city. INTERNAL USE
     * ONLY!
     *
     * @param city
     * @return
     */
    private List<District> getActualCityDistricts(City city) {
        List<District> districts = cityDistrictMap.get(city);
        if (districts == null) {
            districts = new ArrayList<District>();
            cityDistrictMap.put(city, districts);
        }
        return districts;
    }

    /**
     * Gets a list of all districts in a city.
     *
     * @param city
     * @return
     */
    public List<District> getCityDistricts(City city) {
        return new ArrayList(getActualCityDistricts(city));
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
    // LOADING
    ///////////
    /**
     * Loads this CityManager from memory.
     *
     * @return This newly loaded CityManager.
     */
    public CityManager load() {
        return loadCityWorlds().loadCities().loadDistricts().
                loadCityDistrictMappings();
    }

    /**
     * Loads all CityWorlds from files into memory.
     *
     * @return This newly loaded CityManager.
     */
    public CityManager loadCityWorlds() {
        File cityFile = Mafiacraft.getSubFile("geo", "cityworlds.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(cityFile);

        cityWorldMap = new HashMap<World, CityWorld>();

        for (String key : conf.getKeys(false)) {
            Map<String, Object> data = (Map<String, Object>) conf.get(key);
            CityWorld cityWorld = (CityWorld) ConfigurationSerialization.
                    deserializeObject(data, CityWorld.class);

            cityWorldMap.put(cityWorld.getWorld(), cityWorld);
        }

        return this;
    }

    /**
     * Loads all cities from files into memory.
     *
     * @return This newly loaded CityManager.
     */
    public CityManager loadCities() {
        File cityFile = Mafiacraft.getSubFile("geo", "cities.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(cityFile);

        cities = new TIntObjectHashMap<City>();

        for (String key : conf.getKeys(false)) {
            Map<String, Object> data = (Map<String, Object>) conf.get(key);
            City city = (City) ConfigurationSerialization.deserializeObject(data,
                    City.class);

            insertCity(city);
        }

        return this;
    }

    /**
     * Loads all districts from files into memory.
     *
     * @return This newly loaded CityManager.
     */
    public CityManager loadDistricts() {
        File districtFile = Mafiacraft.getSubFile("geo", "districts.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(
                districtFile);

        cityDistrictMap = new HashMap<City, List<District>>();

        for (String key : conf.getKeys(false)) {
            Map<String, Object> data = (Map<String, Object>) conf.get(key);
            District district = (District) ConfigurationSerialization.
                    deserializeObject(data, District.class);

            insertDistrict(district);
        }

        return this;
    }

    /**
     * Loads all city district mappings into memory.
     *
     * @return The city/district mappings.
     */
    public CityManager loadCityDistrictMappings() {
        File mappingFile = Mafiacraft.getSubFile("geo",
                "city_district_mappings.yml");
        YamlConfiguration conf =
                YamlConfiguration.loadConfiguration(mappingFile);

        cityDistrictMap = new HashMap<City, List<District>>();
        districtCityMap = new HashMap<District, City>();

        for (String key : conf.getKeys(false)) {
            City city = getCity(key);

            List<String> dists = conf.getStringList(key);
            for (String distUid : dists) {
                District dist = getDistrictFromUid(distUid);
                attachDistrict(dist, city);
            }
        }

        return this;
    }

    ///////////
    // SAVING
    ///////////
    /**
     * Saves everything in this CityManager.
     *
     * @return This CityManager.
     */
    public CityManager save() {
        return saveCityWorlds().saveCities().saveDistricts().
                saveCityDistrictMappings();
    }

    /**
     * Saves all CityWorlds to the config.
     *
     * @return This City Manager.
     */
    public CityManager saveCityWorlds() {
        File cityFile = Mafiacraft.getSubFile("geo", "cityworlds.yml");
        YamlConfiguration conf = new YamlConfiguration();

        for (CityWorld cityWorld : getCityWorldList()) {
            conf.set(cityWorld.getName(), cityWorld);
        }

        try {
            conf.save(cityFile);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE,
                    "The city world file could not be written for some odd reason!",
                    ex);
        }

        return this;
    }

    /**
     * Saves all cities currently loaded to files.
     *
     * @return This CityManager.
     */
    public CityManager saveCities() {
        File cityFile = Mafiacraft.getSubFile("geo", "cities.yml");
        YamlConfiguration conf = new YamlConfiguration();

        for (City city : getCityList()) {
            conf.set(Integer.toString(city.getId()), city);
        }

        try {
            conf.save(cityFile);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE,
                    "The city file could not be written for some odd reason!",
                    ex);
        }

        return this;
    }

    /**
     * Saves all districts currently loaded to files.
     *
     * @return This CityManager.
     */
    public CityManager saveDistricts() {
        File districtFile = Mafiacraft.getSubFile("geo", "districts.yml");
        YamlConfiguration conf = new YamlConfiguration();

        for (District district : getDistrictList()) {
            conf.set(district.getUid(), district);
        }

        try {
            conf.save(districtFile);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE,
                    "The district file could not be written for some odd reason!",
                    ex);
        }

        return this;
    }

    /**
     * Saves all city/district mappings.
     *
     * @return This CityManager.
     */
    public CityManager saveCityDistrictMappings() {
        File mappingFile = Mafiacraft.getSubFile("geo",
                "city_district_mappings.yml");
        YamlConfiguration conf = new YamlConfiguration();

        for (Entry<City, List<District>> mapping : cityDistrictMap.entrySet()) {
            City city = mapping.getKey();
            List<District> dists = mapping.getValue();
            List<String> distStrs = new ArrayList<String>();
            for (District district : dists) {
                distStrs.add(district.getUid());
            }
            conf.set(Integer.toString(city.getId()), dists);
        }

        try {
            conf.save(mappingFile);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE,
                    "The city/district mapping file could not be written for some odd reason!",
                    ex);
        }

        return this;
    }

}
