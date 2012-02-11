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
    private String name;

    private World world;

    public City(String name, World world) {
        this.name = name;
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public City setName(String name) {
        this.name = name;
        return this;
    }

    public World getWorld() {
        return world;
    }

    public City setWorld(World world) {
        this.world = world;
        return this;
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
