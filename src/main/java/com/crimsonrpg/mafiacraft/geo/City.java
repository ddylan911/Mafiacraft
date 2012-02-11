/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.World;

import com.crimsonrpg.mafiacraft.Mafiacraft;
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
        return Mafiacraft.getInstance().getGovernmentManager().getCityGovernment(this);
    }

    public List<District> getDistricts() {
        return Mafiacraft.getInstance().getCityManager().getCityDistricts(this);
    }

    public boolean hasDistrict(String name) {
        for (District district : getDistricts()) {
            if (district.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public District getDistrict(Chunk chunk) {
        return Mafiacraft.getInstance().getCityManager().getDistrict(chunk);
    }

    public String getNextDistrictName() {
        String chars = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String name = "";

        for (char c : chars.toCharArray()) {
            for (char d : chars.toCharArray()) {
                for (char e : chars.toCharArray()) {
                    name = new StringBuilder(c).append(d).append(e).toString().trim();
                    if (hasDistrict(name)) {
                        continue;
                    }
                }
            }
        }

        return name;
    }

}
