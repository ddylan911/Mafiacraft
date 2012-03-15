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
package net.voxton.mafiacraft.core.city;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import net.voxton.mafiacraft.core.logging.MLogger;
import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.MafiacraftCore;
import net.voxton.mafiacraft.core.player.MPlayer;
import net.voxton.mafiacraft.core.util.GeoUtils;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import net.voxton.mafiacraft.core.geo.MWorld;
import net.voxton.mafiacraft.core.geo.Section;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * Manager for handling district objects.
 */
public class CityManager {

    /**
     * Contains world names mapped to their respective cityworlds.
     */
    private Map<String, MWorld> worldMap =
            new HashMap<String, MWorld>();

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

    /**
     * Map that stores section caches.
     */
    private Map<String, Cache<Long, Section>> sections =
            new HashMap<String, Cache<Long, Section>>();

    private final MafiacraftCore mc;

    /**
     * Constructor.
     *
     * @param mc The MafiacraftPlugin plugin.
     */
    public CityManager(MafiacraftCore mc) {
        this.mc = mc;

        registerSerializations();
    }

    /**
     * Registers all the serializable classes to serialize them.
     */
    private void registerSerializations() {
        ConfigurationSerialization.registerClass(City.class);
        ConfigurationSerialization.registerClass(District.class);
        ConfigurationSerialization.registerClass(MWorld.class);
    }

    /////////////////
    // WORLD
    /////////////////
    /**
     * Gets a city world from its name.
     *
     * @param worldString The name of the world.
     * @return The CityWorld corresponding with the name, or null if there is no
     * such world.
     */
    public MWorld getWorld(String worldString) {
        MWorld cworld = worldMap.get(worldString);
        if (cworld == null) {
            cworld = Mafiacraft.getImpl().getWorld(worldString);
            worldMap.put(worldString, cworld);
        }
        return cworld;
    }

    /**
     * Gets a list of city worlds currently loaded.
     *
     * @return The city world list.
     */
    public List<MWorld> getWorldList() {
        return new ArrayList<MWorld>(worldMap.values());
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
        center.getWorld().setCapital(city);
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
     * Gets the district in the specified world. This will create a district if
     * it needs to.
     *
     * @param world
     * @param x
     * @param z
     * @return
     */
    public District getDistrict(MWorld world, int x, int z) {
        int id = GeoUtils.coordsToDistrictId(x, z);
        District d = getDistrictMap(world).get(id);
        if (d == null) {
            d = (getDistrictList(world).size() <= 0)
                    ? createDistrict(world, x, z)
                    : createDistrict(world, x, z);
        }
        return d;
    }

    public District getDistrict(MWorld world, int id) {
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

        MWorld world = getWorld(split[0]);
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
        MWorld world = district.getWorld();
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
    private District createDistrict(Section sample) {
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
    private District createDistrict(MWorld world, int x, int z) {
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
    private TIntObjectMap<District> getDistrictMap(MWorld world) {
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
    public List<District> getDistrictList(MWorld world) {
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

        MWorld cw = city.getCityWorld();
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
     * Gets a section from its coordinates.
     *
     * @param world The world.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return The section.
     */
    public Section getSection(MWorld world, int x, int y, int z) {
        try {
            return getSectionCache(world).get(getSectionKey(x, y, z));
        } catch (ExecutionException ex) {
            MLogger.log(Level.SEVERE, "Could not retrieve section (" + world
                    + ", " + x + ", " + y + ", " + z + ") from cache.", ex);
        }
        return null;
    }

    /**
     * Gets the section cache of a world.
     *
     * @param world The world of the cache.
     * @return The section cache.
     */
    private Cache<Long, Section> getSectionCache(final MWorld world) {
        Cache<Long, Section> cache = sections.get(world.getName());
        if (cache == null) {
            CacheBuilder builder = CacheBuilder.newBuilder();

            builder.maximumSize(10000).expireAfterWrite(10, TimeUnit.MINUTES);

            cache = builder.build(
                    new CacheLoader<Long, Section>() {

                        @Override
                        public Section load(Long key) throws Exception {
                            int x = (int) ((key & 0xFFFFF80000000000L) >>> 43);
                            int y = (int) ((key & 0x7FFFFC00000L) >>> 22);
                            int z = (int) ((key & 0x3ffffe) >>> 1);
                            return createSection(world, x, y, z);
                        }

                    });
        }
        return cache;
    }

    private Section createSection(MWorld world, int x, int y, int z) {
        return new Section(world, x, y, z);
    }

    /**
     * Gets a section key from an x, y, and z.
     *
     * @param x The x.
     * @param y The y.
     * @param z The z.
     * @return The section key.
     */
    private static long getSectionKey(int x, int y, int z) {
        return ((x & 0x1fffff) << 1)
                | ((y & 0x1fffffL) << 22)
                | ((z & 0x1fffffL) << 43);
    }

    /////////////
    // MISC
    /////////////
    /**
     * Gets a LandOwner based on their id.
     *
     * @param id The id of the landowner
     * @return The LandOwner corresponding with the id
     */
    public LandOwner getLandOwner(String id) {
        if (id == null) {
            return null;
        }

        if (id.length() < 1) {
            return null;
        }

        LandOwner owner = landOwners.get(id);
        if (owner == null) {
            char type = id.charAt(0);

            if (type == 'P') {
                String playerName = id.substring(1);
                return Mafiacraft.getPlayer(playerName);
            }
        }

        return owner;
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
        File cityFile = Mafiacraft.getOrCreateSubFile("geo", "cityworlds.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(cityFile);

        worldMap = new HashMap<String, MWorld>();

        for (String key : conf.getKeys(false)) {
            MWorld cityWorld = (MWorld) conf.get(key);
            worldMap.put(cityWorld.getName(), cityWorld);
        }

        return this;
    }

    /**
     * Loads all cities from files into memory.
     *
     * @return This newly loaded CityManager.
     */
    public CityManager loadCities() {
        File cityFile = Mafiacraft.getOrCreateSubFile("geo", "cities.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(cityFile);

        cities = new TIntObjectHashMap<City>();

        for (String key : conf.getKeys(false)) {
            City city = (City) conf.get(key);

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
        File districtFile =
                Mafiacraft.getOrCreateSubFile("geo", "districts.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(
                districtFile);

        cityDistrictMap = new HashMap<City, List<District>>();

        for (String key : conf.getKeys(false)) {
            District district = (District) conf.get(key);

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
        File mappingFile = Mafiacraft.getOrCreateSubFile("geo",
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
        File cityFile = Mafiacraft.getOrCreateSubFile("geo", "cityworlds.yml");
        YamlConfiguration conf = new YamlConfiguration();

        for (MWorld cityWorld : getWorldList()) {
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
        File cityFile = Mafiacraft.getOrCreateSubFile("geo", "cities.yml");
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
        File districtFile =
                Mafiacraft.getOrCreateSubFile("geo", "districts.yml");
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
        File mappingFile = Mafiacraft.getOrCreateSubFile("geo",
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
