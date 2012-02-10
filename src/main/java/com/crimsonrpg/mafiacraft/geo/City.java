/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.World;

import com.crimsonrpg.mafiacraft.Mafiacraft;

/**
 * Represents a... CITY!
 */
public class City {
    private World world;

    public City() {
    }

    public World getWorld() {
        return world;
    }

    public List<District> getDistricts() {
        return Mafiacraft.getInstance().getDistrictManager().getCityDistricts(this);
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
        return Mafiacraft.getInstance().getDistrictManager().getDistrict(chunk);
    }

    private String getNextName() {
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
