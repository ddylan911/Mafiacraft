/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.World;

import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.gov.Government;

/**
 * Represents a... CITY!
 */
public class City {
    private final int id;

    private String name;

    public City(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public City setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the government of this city.
     * 
     * @return 
     */
    public Government getGovernment() {
        return MafiacraftPlugin.getInstance().getGovernmentManager().getCityGovernment(this);
    }

    /**
     * Gets a list of all districts of this city.
     * 
     * @return 
     */
    public List<District> getDistricts() {
        return MafiacraftPlugin.getInstance().getCityManager().getCityDistricts(this);
    }

    /**
     * Checks if the city contains a district with the specified name.
     * 
     * @param name
     * @return 
     */
    public boolean hasDistrict(String name) {
        for (District district : getDistricts()) {
            if (district.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the district in this city from the specified chunk.
     * 
     * @param chunk
     * @return 
     */
    public District getDistrict(Chunk chunk) {
        return MafiacraftPlugin.getInstance().getCityManager().getDistrict(chunk);
    }

    /**
     * Gets the next free name to give a district.
     * 
     * @return 
     */
    public String getNextDistrictName() {
        String chars = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String dname = "";

        for (char c : chars.toCharArray()) {
            for (char d : chars.toCharArray()) {
                for (char e : chars.toCharArray()) {
                    dname = new StringBuilder(c).append(d).append(e).toString().trim();
                    if (hasDistrict(dname)) {
                        continue;
                    }
                }
            }
        }

        return dname;
    }

}
