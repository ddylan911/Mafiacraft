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
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.gov;

import gnu.trove.iterator.TIntIntIterator;
import net.voxton.mafiacraft.MafiacraftPlugin;
import net.voxton.mafiacraft.geo.City;
import net.voxton.mafiacraft.player.MPlayer;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.Mafiacraft;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * Manages government-related activities.
 */
public class GovernmentManager {

    /**
     * Holds all governments mapped to their ids.
     */
    private TIntObjectMap<Government> governments =
            new TIntObjectHashMap<Government>();

    /**
     * Holds all divisions mapped to their ids.
     */
    private TIntObjectMap<Division> divisions =
            new TIntObjectHashMap<Division>();

    /**
     * Holds all police mappings to their respective cities.
     *
     * The key is the id of the police. The value is the id of the city.
     */
    private TIntIntMap policeMap = new TIntIntHashMap();

    /**
     * Maps governments to divisions. One to many.
     */
    private Map<Government, List<Division>> govDivMap =
            new HashMap<Government, List<Division>>();

    /**
     * Maps divisions to governments. Many to one.
     */
    private Map<Division, Government> divGovMap =
            new HashMap<Division, Government>();

    /**
     * Hook to the main plugin.
     */
    private MafiacraftPlugin mc;

    /**
     * Constructor.
     *
     * @param mc The MafiacraftPlugin object.
     */
    public GovernmentManager(MafiacraftPlugin mc) {
        this.mc = mc;

        registerSerializations();
    }

    /**
     * Registers serializations.
     */
    private void registerSerializations() {
        ConfigurationSerialization.registerClass(Government.class);
        ConfigurationSerialization.registerClass(Division.class);
    }

    /**
     * Gets a list of all governments.
     *
     * @return A list of all governments.
     */
    public List<Government> getGovernmentList() {
        return new ArrayList<Government>(governments.valueCollection());
    }

    /**
     * Gets the government associated with the id.
     *
     * @param id The id of the government.
     * @return The Government associated with the id.
     */
    public Government getGovernment(int id) {
        return governments.get(id);
    }

    /**
     * Gets a government by its name. Case insensitive.
     *
     * @param name
     * @return
     */
    public Government getGovernment(String name) {
        for (Government government : getGovernmentList()) {
            if (government.getName().equalsIgnoreCase(name)) {
                return government;
            }
        }
        return null;
    }

    /**
     * Gets the government of the specified city.
     *
     * @param city
     * @return
     */
    public Government getPolice(City city) {
        return getGovernment(policeMap.get(city.getId()));
    }

    /**
     * Creates a new government.
     *
     * @param name The name of the government.
     * @return
     */
    public Government createGovernment(String name, GovType type) {
        int id = getNextGovernmentId();
        Government government = new Government(id);
        insertGovernment(government);
        government.setName(name).setType(type);
        return government;
    }

    /**
     * Get the government of a player.
     *
     * @param player
     * @return
     */
    public Government getGovernment(MPlayer player) {
        for (Government gov : getGovernmentList()) {
            if (gov.isMember(player)) {
                return gov;
            }
        }
        return null;
    }

    /**
     * Sets the city government of a city.
     *
     * @param city
     * @param government
     */
    public void setPolice(City city, Government government) {
        government.setType(GovType.POLICE);
        policeMap.put(city.getId(), government.getId());
    }

    /**
     * Gets a list of divisions of a certain government.
     *
     * @param gov The government.
     * @return A list of divisions.
     */
    public List<Division> getDivisions(Government gov) {
        return new ArrayList<Division>(getActualDivisionList(gov));
    }

    /**
     * Gets the next available and unused government id.
     *
     * @return The government id.
     */
    private int getNextGovernmentId() {
        int id = 0;
        for (int i = 1; getGovernment(i) != null; ++i) {
            id = i;
        }
        return id;
    }

    /**
     * Gets THE list of divisions pertaining to the given government. INTERNAL
     * USE ONLY!!
     *
     * @param gov The government.
     * @return The list of divisions.
     */
    private List<Division> getActualDivisionList(Government gov) {
        List<Division> divs = govDivMap.get(gov);
        if (divs == null) {
            divs = new ArrayList<Division>();
            govDivMap.put(gov, divs);
        }
        return divs;
    }

    ///////////
    // DIVISION METHODS
    ///////////
    /**
     * Gets a division.
     *
     * @param id
     * @return
     */
    public Division getDivision(int id) {
        return divisions.get(id);
    }

    /**
     * Gets a list of all divisions on the server.
     *
     * @return
     */
    public List<Division> getDivisionList() {
        return new ArrayList<Division>(divisions.valueCollection());
    }

    /**
     * Creates a division. (No validation)
     *
     * @param gov
     * @return
     */
    public Division createDivision(Government gov) {
        int id = getNextDivisionId();
        Division div = new Division(id);
        insertDivision(div).bindDivision(div, gov);
        return div;
    }

    /**
     * Inserts a division into the maps.
     *
     * @param div The division to insert.
     * @return This manager.
     */
    public GovernmentManager insertDivision(Division div) {
        divisions.put(div.getId(), div);
        mc.getCityManager().registerLandOwner(div);
        return this;
    }

    /**
     * Inserts a government into this GovernmentManager.
     *
     * @param gov The government to insert.
     * @return This GovernmentManager.
     */
    public GovernmentManager insertGovernment(Government gov) {
        governments.put(gov.getId(), gov);
        mc.getCityManager().registerLandOwner(gov);
        return this;
    }

    /**
     * Binds a division to a government.
     *
     * @param div The division to make a binding for.
     * @param gov The government to bind with.
     * @return This GovernmentManager.
     */
    public GovernmentManager bindDivision(Division div, Government gov) {
        getActualDivisionList(gov).add(div);
        divGovMap.put(div, gov);
        return this;
    }

    /**
     * Removes all references to the given division from the Government manager.
     *
     * @param div
     * @return
     */
    public boolean removeDivision(Division div) {
        Government gov = div.getGovernment();
        if (gov == null) {
            return false;
        }

        divisions.remove(div.getId());
        getActualDivisionList(gov).remove(div);
        divGovMap.remove(div);

        return true;
    }

    /**
     * Gets the government of the specified division.
     *
     * @param div The government of the division.
     * @return The division.
     */
    public Government getGovernmentOf(Division div) {
        return divGovMap.get(div);
    }

    /**
     * Gets the next available ID for a division.
     *
     * @return The next available District id.
     */
    private int getNextDivisionId() {
        int id = 0;
        for (int i = 1; getDivision(i) != null; ++i) {
            id = i;
        }
        return id;
    }

    //////////////
    // LOADING
    //////////////
    /**
     * Loads everything in this government manager.
     *
     * @return This government manager.
     */
    public GovernmentManager load() {
        return loadGovernments().loadDivisions().loadPoliceMappings().
                loadDivGovMappings();
    }

    /**
     * Loads all governments into memory.
     *
     * @return This GovernmentManager.
     */
    public GovernmentManager loadGovernments() {
        File govFile = Mafiacraft.getSubFile("gov", "governments.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(govFile);

        governments = new TIntObjectHashMap<Government>();

        for (String key : conf.getKeys(false)) {
            Government gov = (Government) conf.get(key);

            insertGovernment(gov);
        }

        return this;
    }

    /**
     * Loads all divisions into memory.
     *
     * @return This GovernmentManager.
     */
    public GovernmentManager loadDivisions() {
        File divFile = Mafiacraft.getSubFile("gov", "divisions.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(divFile);

        divisions = new TIntObjectHashMap<Division>();

        for (String key : conf.getKeys(false)) {
            Division div = (Division) conf.get(key);

            insertDivision(div);
        }

        return this;
    }

    /**
     * Loads all police/city mappings into memory.
     *
     * @return This GovernmentManager.
     */
    public GovernmentManager loadPoliceMappings() {
        File policeFile = Mafiacraft.getSubFile("gov", "police.yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(policeFile);

        policeMap = new TIntIntHashMap();

        for (String key : conf.getKeys(false)) {
            int value = conf.getInt(key);

            int policeId = 0;
            try {
                policeId = Integer.parseInt(key);
            } catch (NumberFormatException ex) {
                MLogger.log(Level.SEVERE, "Invalid police id '" + key + "'!", ex);
            }

            policeMap.put(policeId, value);
        }

        return this;
    }

    /**
     * Loads all div/gov mappings into memory.
     *
     * @return This GovernmentManager.
     */
    public GovernmentManager loadDivGovMappings() {
        File mappingFile = Mafiacraft.getSubFile("gov",
                "division_government_mappings.yml");
        YamlConfiguration conf =
                YamlConfiguration.loadConfiguration(mappingFile);

        divGovMap = new HashMap<Division, Government>();
        govDivMap = new HashMap<Government, List<Division>>();

        for (String key : conf.getKeys(false)) {
            int gid = 0;
            try {
                gid = Integer.parseInt(key);
            } catch (NumberFormatException ex) {
                MLogger.log(Level.SEVERE, "Unexpected non-number found when "
                        + "parsing div/gov mappings! String: '" + key + "'", ex);
            }
            Government gov = getGovernment(gid);
            if (gov == null) {
                MLogger.log(Level.WARNING, "Government with id '" + gid
                        + "' does not exist; skipping...");
                continue;
            }

            List<String> val = conf.getStringList(key);

            for (String divId : val) {
                int did = 0;
                try {
                    did = Integer.parseInt(divId);
                } catch (NumberFormatException ex) {
                    MLogger.log(Level.SEVERE, "Unexpected string literal as opposed to a number found when parsing div/gov mappings! String is '"
                            + divId + "'!", ex);
                }

                Division div = getDivision(did);
                if (div == null) {
                    MLogger.log(Level.WARNING, "Division with id '" + did
                            + "' does not exist; skipping...");
                    continue;
                }

                bindDivision(div, gov);
            }
        }

        return this;
    }
    //////////////
    // SAVING
    //////////////

    /**
     * Saves everything in this government manager.
     *
     * @return This government manager.
     */
    public GovernmentManager save() {
        return saveGovernments().saveDivisions().savePoliceMappings().
                saveDivGovMappings();
    }

    /**
     * Saves all governments to files.
     *
     * @return This GovernmentManager.
     */
    public GovernmentManager saveGovernments() {
        File govFile = Mafiacraft.getSubFile("gov", "governments.yml");
        YamlConfiguration conf = new YamlConfiguration();

        for (Government gov : getGovernmentList()) {
            conf.set(Integer.toString(gov.getId()), gov);
        }

        try {
            conf.save(govFile);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE,
                    "The government file could not be written for some odd reason!",
                    ex);
        }

        return this;
    }

    /**
     * Saves all divisions to files.
     *
     * @return This GovernmentManager.
     */
    public GovernmentManager saveDivisions() {
        File divFile = Mafiacraft.getSubFile("gov", "divisions.yml");
        YamlConfiguration conf = new YamlConfiguration();

        for (Division div : getDivisionList()) {
            conf.set(Integer.toString(div.getId()), div);
        }

        try {
            conf.save(divFile);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE,
                    "The division file could not be written for some odd reason!",
                    ex);
        }

        return this;
    }

    /**
     * Saves all police mappings to cities.
     *
     * @return This GovernmentManager.
     */
    public GovernmentManager savePoliceMappings() {
        File policeFile = Mafiacraft.getSubFile("gov", "police.yml");
        YamlConfiguration conf = new YamlConfiguration();

        TIntIntIterator policeIterator = policeMap.iterator();

        while (policeIterator.hasNext()) {
            policeIterator.advance();
            conf.set(Integer.toString(policeIterator.key()),
                    Integer.toString(policeIterator.value()));
        }

        try {
            conf.save(policeFile);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE,
                    "The police mapping file could not be written for some odd reason!",
                    ex);
        }

        return this;
    }

    /**
     * Saves all mappings between divisions and governments. Mappings are saved
     * in this format:
     *
     * <pre>
     * government:
     *  -div
     *  -div2
     * </pre>
     *
     * and so on.
     *
     * @return This GovernmentManager.
     */
    public GovernmentManager saveDivGovMappings() {
        File mappingFile = Mafiacraft.getSubFile("gov",
                "division_government_mappings.yml");
        YamlConfiguration conf = new YamlConfiguration();

        for (Entry<Government, List<Division>> mapping : govDivMap.entrySet()) {
            Government gov = mapping.getKey();
            List<Division> divs = mapping.getValue();
            List<String> divStrs = new ArrayList<String>();
            for (Division div : divs) {
                divStrs.add(Integer.toString(div.getId()));
            }
            conf.set(Integer.toString(gov.getId()), divStrs);
        }

        try {
            conf.save(mappingFile);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE,
                    "The gov/div mapping file could not be written for some odd reason!",
                    ex);
        }

        return this;
    }

}
