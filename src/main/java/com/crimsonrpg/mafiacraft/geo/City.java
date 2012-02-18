/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.geo;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.vault.Transactable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 * Represents a... CITY!
 */
public class City extends Transactable implements LandOwner {
    private final int id;

    private String name;

    private String mayor;

    private Set<String> advisors;

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
     * Gets the police of this city.
     *
     * @return
     */
    public Government getPolice() {
        return Mafiacraft.getGovernmentManager().getPolice(this);
    }

    /**
     * Gets a list of all districts of this city.
     *
     * @return
     */
    public List<District> getDistricts() {
        return Mafiacraft.getCityManager().getCityDistricts(this);
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
        return Mafiacraft.getDistrict(chunk);
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

    /**
     * Gets all players in the city.
     *
     * @return
     */
    public List<MPlayer> getPlayers() {
        List<MPlayer> players = new ArrayList<MPlayer>();
        for (District district : getDistricts()) {
            players.addAll(district.getPlayers());
        }
        return players;
    }

    /**
     * Gets the mayor of the city.
     *
     * @return
     */
    public String getMayor() {
        return mayor;
    }

    /**
     * Gets the mayor of the city if online.
     *
     * @return
     */
    public MPlayer getMayorIfOnline() {
        Player p = Bukkit.getPlayer(mayor);
        if (p != null) {
            return Mafiacraft.getPlayer(p);
        }
        return null;
    }

    /**
     * Sets the mayor of the city. Case sensitive!
     *
     * @param mayor
     */
    public void setMayor(String mayor) {
        this.mayor = mayor;
    }

    /**
     * Checks if the given player is mayor.
     *
     * @param name
     * @return
     */
    public boolean isMayor(String name) {
        return mayor.equals(name);
    }

    /**
     * Gets a list of all advisors in the city.
     *
     * @return
     */
    public List<String> getAdvisors() {
        return new ArrayList<String>(advisors);
    }

    /**
     * Adds an advisor to the city.
     *
     * @param advisor
     * @return True if the operation was successful
     */
    public boolean addAdvisor(String advisor) {
        return advisors.add(advisor);
    }

    /**
     * Adds an MPlayer advisor to the city.
     *
     * @param player
     * @return True if the operation was successful
     */
    public boolean addAdvisor(MPlayer player) {
        return addAdvisor(player.getName());
    }

    /**
     * Removes an advisor from the city.
     *
     * @param advisor
     * @return True if the operation was successful
     */
    public boolean removeAdvisor(String advisor) {
        return advisors.remove(advisor);
    }

    /**
     * Removes an advisor from the city.
     *
     * @param advisor
     * @return True if the operation was successful
     */
    public boolean removeAdvisor(MPlayer advisor) {
        return removeAdvisor(advisor.getName());
    }

    /**
     * Returns true if the given player is an advisor for the city.
     *
     * @param name
     * @return
     */
    public boolean isAdvisor(String name) {
        return advisors.contains(name);
    }

    /**
     * Returns true if the given player is in the government.
     *
     * @param name
     * @return
     */
    public boolean isInGovernment(String name) {
        return isAdvisor(name) || isMayor(name);
    }

    /**
     * Gets a list of all members of the city.
     *
     * @return
     */
    public List<String> getMembers() {
        List<String> members = getAdvisors();
        members.add(getMayor());
        return members;
    }

    //////// 
    // Land owner stuff
    ////////
    public OwnerType getOwnerType() {
        return OwnerType.CITY;
    }

    public String getOwnerName() {
        return "The City of " + name;
    }

    public String getOwnerId() {
        return "C-" + id;
    }

    public boolean canBuild(MPlayer player, Chunk chunk) {
        return false; //TODO: city government
    }

    public boolean canBeClaimed(Chunk chunk, LandOwner futureOwner) {
        return false;
    }

}
