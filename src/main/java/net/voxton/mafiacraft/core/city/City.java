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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.gov.GovType;
import net.voxton.mafiacraft.core.gov.Government;
import net.voxton.mafiacraft.core.player.MPlayer;
import net.voxton.mafiacraft.core.chat.MsgColor;
import net.voxton.mafiacraft.core.econ.Transactable;
import java.util.logging.Level;
import net.voxton.mafiacraft.core.geo.MPoint;
import net.voxton.mafiacraft.core.geo.MWorld;
import net.voxton.mafiacraft.core.geo.Section;
import net.voxton.mafiacraft.core.util.logging.MLogger;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * Represents a... CITY!
 */
@SerializableAs("city")
public class City extends Transactable implements LandOwner, ConfigurationSerializable, Serializable {

    private int id;

    private String name;

    private String mayor;

    private Set<String> advisors = new HashSet<String>();

    private MPoint spawn;

    private MWorld cityWorld;

    public City() {
    }

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
     * Sets the police of the city.
     *
     * @param police
     * @return
     */
    public City setPolice(Government police) {
        Mafiacraft.getGovernmentManager().setPolice(this, police);
        return this;
    }

    /**
     * Establishes a police force within the city.
     *
     * @param chief The chief of police
     * @param assistant The assistant chief
     * @return The police force created
     */
    public Government establishPolice(MPlayer chief, MPlayer assistant) {
        Government police = Mafiacraft.getGovernmentManager().createGovernment(
                getName(), GovType.POLICE);
        setPolice(police);
        police.setLeader(chief).setViceLeader(assistant);
        return police;
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
        return getDistrictByName(name) != null;
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
                    dname = new StringBuilder(e).append(d).append(c).toString().
                            trim();
                    if (!(dname.isEmpty() || hasDistrict(dname))) {
                        return dname;
                    }
                }
            }
        }

        throw new IllegalStateException("Too many districts in the city!");
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
        return Mafiacraft.getOnlinePlayer(getMayor());
    }

    /**
     * Sets the mayor of the city. Case sensitive!
     *
     * @param mayor
     * @return This city
     */
    public City setMayor(String mayor) {
        this.mayor = mayor;
        return this;
    }

    /**
     * Checks if the given player is mayor.
     *
     * @param name
     * @return
     */
    public boolean isMayor(String name) {
        return mayor != null && mayor.equals(name);
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
     * Sets the advisors of the city.
     *
     * @param advisors The advisors to set
     * @return This city.
     */
    private City setAdvisors(Set<String> advisors) {
        this.advisors = advisors;
        return this;
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
        return advisors != null && advisors.contains(name);
    }

    /**
     * Returns true if the given player is a member of the government.
     *
     * @param name
     * @return
     */
    public boolean isMember(String name) {
        return isAdvisor(name) || isMayor(name);
    }

    /**
     * Returns true if the given player is a member of the government.
     *
     * @param player
     * @return
     */
    public boolean isMember(MPlayer player) {
        return isMember(player.getName());
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
        return "C" + id;
    }

    public boolean canBuild(MPlayer player, Section section) {
        return isMember(player);
    }

    public boolean canBeClaimed(Section section, LandOwner futureOwner) {
        return false;
    }

    /**
     * Attaches a new district to this city.
     *
     * @param district
     * @return
     */
    public String attachNewDistrict(District district) {
        district.setCity(this);
        String newName = getNextDistrictName();
        district.setName(newName);
        return newName;
    }

    /**
     * Returns true if the given player is a mayor of the city.
     *
     * @param player
     * @return
     */
    public boolean isMayor(MPlayer player) {
        return isMayor(player.getName());
    }

    /**
     * Sets the spawn location of the city.
     *
     * @param point
     */
    public void setSpawnPoint(MPoint point) {
        this.spawn = point;
    }

    /**
     * Gets the spawn location of the city.
     *
     * @return
     */
    public MPoint getSpawnPoint() {
        return spawn;
    }

    /**
     * Disbands the city. This method removes all references to the city
     * anywhere.
     *
     * @return
     */
    public City disband() {
        Mafiacraft.getCityManager().disbandCity(this);
        return this;
    }

    /**
     * Gets a district of this city by name.
     *
     * @param districtName The district name.
     * @return The district if it exists.
     */
    public District getDistrictByName(String districtName) {
        for (District district : getDistricts()) {
            String n = district.getName();
            if (n != null && n.equalsIgnoreCase(districtName)) {
                return district;
            }
        }
        return null;
    }

    /**
     * Gets the city world of the world.
     *
     * @return
     */
    public MWorld getCityWorld() {
        return cityWorld;
    }

    /**
     * Sets the city world.
     *
     * @param cityWorld
     */
    public City setCityWorld(MWorld cityWorld) {
        this.cityWorld = cityWorld;
        return this;
    }

    /**
     * Claims a grid for the given district with checking.
     *
     * @param district
     * @return True if the district was part of the city and could be claimed.
     */
    public boolean claimGridAndCheck(District district) {
        if (!district.getCity().equals(this)) {
            return false;
        }

        claimGrid(district);
        return true;
    }

    /**
     * Claims a grid.
     *
     * @param district
     * @return
     */
    public City claimGrid(District district) {
        for (int x = 0; x < 15; x++) {
            for (int z = 0; z < 15; z++) {
                if (x == 0 || z == 0 || x == 15 || z == 15 || (x + 1) % 5 == 0
                        || (z + 1) % 5 == 0) {
                    district.setOwner(x, z, this);
                }
            }
        }
        return this;
    }

    public String getEntryMessage() {
        return MsgColor.INFO + "The City of " + getName();
    }

    /////////////
    // SERIALIZATION
    /////////////
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id", getId());
        data.put("name", getName());
        data.put("spawn", getSpawnPoint().serializeToString());
        data.put("world", getCityWorld().getName());

        data.put("mayor", getMayor());
        data.put("advisors", getAdvisors());

        return data;
    }

    /**
     * Deserializes a City.
     *
     * @param data The data in Map form.
     * @return The deserialized City.
     */
    public static City deserialize(Map<String, Object> data) {
        int id = 0;
        String strId = data.get("id").toString();
        try {
            id = Integer.parseInt(strId);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE,
                    "Invalid number encountered when deserializing a city!", ex);
        }

        City city = new City(id);

        String name = data.get("name").toString();

        String spawnS = data.get("spawn").toString();
        MPoint spawn = MPoint.deserialize(spawnS);

        String worldS = data.get("world").toString();
        MWorld cworld = Mafiacraft.getCityManager().getWorld(worldS);

        String mayor = data.get("mayor").toString();

        List<String> advisorsS = (List<String>) data.get("advisors");
        Set<String> advisors = new HashSet<String>(advisorsS);

        //Set info
        city.setName(name).setSpawnPoint(spawn);
        city.setCityWorld(cworld);

        //Set members
        city.setMayor(mayor).setAdvisors(advisors);

        return city;
    }

}
