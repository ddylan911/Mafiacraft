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
     * Gets the government of the specified city.
     * 
     * @param city
     * @return 
     */
    public Government getCityGovernment(City city) {
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
    public void setCityGovernment(City city, Government government) {
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

}
