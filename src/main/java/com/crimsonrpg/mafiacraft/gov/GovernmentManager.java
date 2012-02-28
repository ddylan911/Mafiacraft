/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.geo.City;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simplyianm
 */
public class GovernmentManager {
    private TIntObjectMap<Government> governments = new TIntObjectHashMap<Government>();

    private TIntObjectMap<Division> divisions = new TIntObjectHashMap<Division>();

    private TIntIntMap cities = new TIntIntHashMap();

    private MafiacraftPlugin mc;

    public GovernmentManager(MafiacraftPlugin mc) {
        this.mc = mc;
    }

    public List<Government> getGovernments() {
        return new ArrayList<Government>(governments.valueCollection());
    }

    /**
     * Gets the government associated with the id.
     *
     * @param id
     * @return
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
        for (Government government : getGovernments()) {
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
        return getGovernment(cities.get(city.getId()));
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
        governments.put(id, government);
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
        for (Government gov : getGovernments()) {
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
        cities.put(city.getId(), government.getId());
    }

    private int getNextGovernmentId() {
        int id = 0;
        for (int i = 1; getGovernment(i) != null; ++i) {
            id = i;
        }
        return id;
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
    public List<Division> getDivisions() {
        return new ArrayList<Division>(divisions.valueCollection());
    }

    /**
     * Gets a list of all divisions in a government.
     *
     * @param gov
     * @return
     */
    public List<Division> getGovDivisions(Government gov) {
        List<Division> divisions = new ArrayList<Division>();
        for (Division division : getDivisions()) {
            if (division.getGovernment().equals(gov)) {
                divisions.add(division);
            }
        }
        return divisions;
    }

    /**
     * Creates a division. (No validation)
     *
     * @param gov
     * @return
     */
    public Division createDivision(Government gov) {
        int id = getNextDivisionId();
        Division div = new Division(id, gov);
        divisions.put(id, div);
        return div;
    }

    /**
     * Gets the next available ID for a division.
     *
     * @return
     */
    private int getNextDivisionId() {
        int id = 0;
        for (int i = 1; getDivision(i) != null; ++i) {
            id = i;
        }
        return id;
    }

}
