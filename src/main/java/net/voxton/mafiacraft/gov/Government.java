/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.gov;

import net.voxton.mafiacraft.geo.LandOwner;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.geo.LandPurchaser;
import net.voxton.mafiacraft.geo.OwnerType;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.vault.Transactable;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.util.LocationSerializer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * Represents a Government, a mafia or a police.
 */
@SerializableAs("gov")
public class Government extends Transactable implements LandPurchaser, ConfigurationSerializable {
    private final int id;

    private String name;

    private GovType type;

    private String leader = null;

    private String viceLeader = null;

    private Set<String> officers = new HashSet<String>();

    private Set<String> affiliates = new HashSet<String>();

    private Location hq;

    /**
     * Holds the land of the government. (Not divisions!)
     */
    private int land;

    public Government(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Gets the name of this government.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the total amount of land the government owns.
     *
     * @return
     */
    public int getTotalLand() {
        int totalLand = getLand();
        for (Division division : getDivisions()) {
            totalLand += division.getLand();
        }
        return totalLand;
    }

    /**
     * Returns true if the Government HQ can be claimed.
     *
     * @return
     */
    public boolean canHQBeClaimed() {
        return getLand() > getTotalLand();
    }

    /**
     * Gets the power the government has over its land. For now, this is the
     * same as getMaxPower().
     *
     * <p>NOTE: In my opinion, it should be lessened based on other factors so
     * it's actually possible to claim other governments after wars or
     * something.</p>
     */
    public int getPower() {
        int power = 0;
        for (MPlayer player : getCouncilMembersAsMPlayers()) {
            power += player.getPower();
        }
        return power;
    }

    /**
     * Gets the minimum amount of power a player can have in the government.
     *
     * @return
     */
    public int getMinPlayerPower() {
        return -getMaxPlayerPower();
    }

    /**
     * Gets the maximum player power of the goverment.
     *
     * @return
     */
    public int getMaxPlayerPower() {
        return getPlayerPower() << 1;
    }

    /**
     * Gets the "P" variable as described in the Google doc:
     *
     * <p>P = (L / players)</p>
     *
     * @return
     */
    public int getPlayerPower() {
        return getMaxGovernmentLand() / getCouncilMemberCount();
    }

    /**
     * Sets the name of this government.
     *
     * @param name
     * @return
     */
    public Government setName(String name) {
        this.name = name;
        return this;
    }

    public GovType getType() {
        return type;
    }

    public void setType(GovType type) {
        this.type = type;
    }

    public boolean canBuild(MPlayer player, Chunk c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getOwnerName() {
        return name;
    }

    ///////////////
    // POSITION METHODS
    ///////////////
    /**
     * Gets the leader of the government.
     *
     * @return
     */
    public String getLeader() {
        return leader;
    }

    /**
     * Gets the vice leader of the government.
     *
     * @return
     */
    public String getViceLeader() {
        return viceLeader;
    }

    /**
     * Gets a list of all members in the government.
     *
     * @return
     */
    public List<String> getMembers() {
        List<String> members = new ArrayList<String>();
        for (Position position : Position.values()) {
            members.addAll(getMembers(position));
        }
        return members;
    }

    /**
     * Gets a list of all members in the government's council.
     *
     * @return
     */
    public List<String> getCouncilMembers() {
        List<String> members = new ArrayList<String>();
        members.add(leader);
        members.add(viceLeader);
        members.addAll(getMembers(Position.OFFICER));
        return members;
    }

    /**
     * Gets a map of all positions.
     *
     * @return
     */
    public Map<Position, List<String>> getPositions() {
        Map<Position, List<String>> positions = new EnumMap<Position, List<String>>(Position.class);
        for (Position position : Position.values()) {
            positions.put(position, getMembers(position));
        }
        return positions;
    }

    /**
     * Gets a list of all members in the specified position.
     *
     * @param position
     * @return
     */
    public List<String> getMembers(Position position) {
        List<String> members = new ArrayList<String>();
        switch (position) {
            case WORKER:
                for (Division division : getDivisions()) {
                    members.addAll(division.getWorkers());
                }
                break;

            case MANAGER:
                for (Division division : getDivisions()) {
                    members.add(division.getManager());
                }
                break;

            case LEADER:
                if (leader != null) {
                    members.add(leader);
                }
                break;

            case VICE_LEADER:
                if (viceLeader != null) {
                    members.add(viceLeader);
                }
                break;

            case OFFICER:
                members.addAll(officers);
                break;

            case AFFILIATE:
                members.addAll(affiliates);
                break;
        }
        return members;
    }

    /**
     * Gets the division a certain player is part of.
     *
     * @param player
     * @return
     */
    public Division getDivision(String player) {
        for (Division division : getDivisions()) {
            if (division.isMember(player)) {
                return division;
            }
        }
        return null;
    }

    /**
     * Gets the division the player is part of.
     *
     * @param player
     * @return
     */
    public Division getDivision(MPlayer player) {
        return getDivision(player.getName());
    }

    /**
     * Gets a list of all divisions in this Government.
     *
     * @return
     */
    public List<Division> getDivisions() {
        return Mafiacraft.getGovernmentManager().getDivisions(this);
    }

    /**
     * Gets a division by its name.
     *
     * @param name
     * @return
     */
    public Division getDivisionByName(String name) {
        for (Division division : getDivisions()) {
            if (division.getName().equalsIgnoreCase(name)) {
                return division;
            }
        }
        return null;
    }

    /**
     * Creates a division for this government. (No validation)
     *
     * @return
     */
    public Division createDivision() {
        return Mafiacraft.getGovernmentManager().createDivision(this);
    }

    /**
     * Gets the amount of council members in the Government.
     *
     * @return
     */
    public int getCouncilMemberCount() {
        return getCouncilMembers().size();
    }

    /**
     * Gets the total member count of the entire government.
     *
     * @return
     */
    public int getMemberCount() {
        return getMembers().size();
    }

    /**
     * Gets the total member count of a position.
     *
     * @param position
     * @return
     */
    public int getMemberCount(Position position) {
        return getMembers(position).size();
    }

    /**
     * Gets a list of all online members of this government.
     *
     * @return
     */
    public List<MPlayer> getOnlineMembers() {
        List<MPlayer> members = new ArrayList<MPlayer>();
        for (MPlayer player : Mafiacraft.getOnlinePlayers()) {
            if (isMember(player)) {
                members.add(player);
            }
        }
        return members;
    }

    /**
     * Gets all of the online members in a position.
     *
     * @param position
     * @return
     */
    public List<MPlayer> getOnlineMembers(Position position) {
        List<MPlayer> members = new ArrayList<MPlayer>();
        List<String> pos = getMembers(position);
        for (MPlayer player : Mafiacraft.getOnlinePlayers()) {
            if (pos.contains(player.getName())) {
                members.add(player);
            }
        }
        return members;
    }

    /**
     * Gets the amount of members currently online the server.
     *
     * @return
     */
    public int getOnlineMemberCount() {
        return getOnlineMembers().size();
    }

    /**
     * Returns true if the government can have more players in a position.
     *
     * @param position
     * @return
     */
    public boolean canHaveMore(Position position) {
        int count = getMemberCount(position);
        return (count < position.getLimit(this));
    }

    public boolean canHaveLess(Position position) {
        if (position.isDivision()) {
            return true;
        }

        int memberCount = getMemberCount(position);

        if (memberCount <= position.getMinimum(this)) {
            return false;
        }

        return true;
    }

    /**
     * Gets the position of a certain player.
     *
     * @param player
     * @return
     */
    public Position getPosition(String player) {
        Position pos = Position.NONE;
        for (Entry<Position, List<String>> position : getPositions().entrySet()) {
            if (position.getValue().contains(player)) {
                return position.getKey();
            }
        }
        return pos;
    }

    /**
     * Gets the position of a certain MPlayer.
     *
     * @param player
     * @return
     */
    public Position getPosition(MPlayer player) {
        return getPosition(player.getName());
    }

    /**
     * Unseats the leader from the government.
     *
     * @return
     */
    public Government unseatLeader() {
        addAffiliate(leader);
        succeedLeader();
        return this;
    }

    /**
     * Unseats the vice leader from office.
     *
     * @return
     */
    public Government unseatViceLeader() {
        addAffiliate(viceLeader);
        viceLeader = null;
        return this;
    }

    /**
     * Makes the vice leader succeed the leader.
     *
     * @return
     */
    public Government succeedLeader() {
        setLeader(viceLeader);
        unseatViceLeader();
        return this;
    }

    /**
     * Adds an officer to the government.
     *
     * @param player
     * @return
     */
    public Government addOfficer(MPlayer player) {
        return addOfficer(player.getName());
    }

    /**
     * Adds an officer to the government.
     *
     * @param player
     * @return
     */
    public Government addOfficer(String player) {
        officers.add(player);
        return this;
    }

    /**
     * Sets the government's HQ.
     *
     * @param hq
     * @return
     */
    public Government setHq(Location hq) {
        this.hq = hq;
        return this;
    }

    /**
     * Sets the vice leader of the government.
     *
     * @param player
     * @return
     */
    public Government setLeader(MPlayer player) {
        return setLeader(player.getName());
    }

    /**
     * Sets the leader of the government.
     *
     * @param leader
     * @return
     */
    public Government setLeader(String leader) {
        this.leader = leader;
        return this;
    }

    /**
     * Sets the vice leader of the government.
     *
     * @param viceLeader
     * @return
     */
    public Government setViceLeader(MPlayer viceLeader) {
        return setViceLeader(viceLeader.getName());
    }

    /**
     * Sets the vice leader of the government.
     *
     * @param viceLeader
     * @return
     */
    public Government setViceLeader(String viceLeader) {
        this.viceLeader = viceLeader;
        return this;
    }

    /**
     * Checks if the given member can be removed from the government
     * legitimately.
     *
     * @param player
     * @return
     */
    public boolean canRemoveMember(MPlayer player) {
        return canHaveLess(player.getPosition());
    }

    /**
     * Removes a member from this government.
     *
     * @param player
     */
    public Government removeMember(MPlayer player) {
        switch (player.getPosition()) {
            case LEADER:
                leader = null;
                break;

            case VICE_LEADER:
                viceLeader = null;
                break;

            case OFFICER:
                officers.remove(player.getName());
                break;

            case MANAGER:
            case WORKER:
                player.getDivision().remove(player.getName());
                break;

            case AFFILIATE:
                affiliates.remove(player.getName());
                break;

            case NONE:
            default:
                break;
        }
        return this;
    }

    /**
     * Preferred way of removing members.
     *
     * @param player
     * @return
     */
    public Government removeMemberAndSucceed(MPlayer player) {
        switch (player.getPosition()) {
            case LEADER:
                if (viceLeader != null) {
                    unseatLeader();
                }
                break;

            case VICE_LEADER:
                unseatViceLeader();
                break;

            default:
                break;
        }

        removeMember(player);
        return this;
    }

    /**
     * Returns true if the player is a member of this government.
     *
     * @param player
     * @return
     */
    public boolean isMember(String player) {
        return getMembers().contains(player);
    }

    /**
     * Returns true if the player is a member of this government.
     *
     * @param player
     * @return
     */
    public boolean isMember(MPlayer player) {
        return isMember(player.getName());
    }

    /**
     * Adds a member to this government as an affiliate.
     *
     * @param player
     * @return True if the operation was allowed.
     */
    public boolean addAffiliate(String player) {
        return affiliates.add(player);
    }

    /**
     * Adds a member to this government.
     *
     * @param player
     * @param position
     * @return True if the operation was allowed.
     */
    public boolean addAffiliate(MPlayer player) {
        return addAffiliate(player.getName());
    }

    /**
     * Gets a list of all affiliates within the Government.
     *
     * @return The affiliates associated with this government.
     */
    public List<String> getAffiliates() {
        return new ArrayList<String>(affiliates);
    }

    /**
     * Sets the affiliates of the Government.
     *
     * @param affiliates The affiliates to set
     * @return The Government
     */
    private Government setAffiliates(Set<String> affiliates) {
        this.affiliates = affiliates;
        return this;
    }

    /**
     * Gets a list of all officers.
     *
     * @return
     */
    public List<String> getOfficers() {
        return new ArrayList<String>(officers);
    }

    /**
     * Sets the officers of the Government.
     *
     * @param officers The officers to set
     * @return The Government
     */
    private Government setOfficers(Set<String> officers) {
        this.officers = officers;
        return this;
    }

    /**
     * Gets a list of all officers currently online.
     *
     * @return The list of officers.
     */
    public List<MPlayer> getOnlineOfficers() {
        List<MPlayer> online = new ArrayList<MPlayer>();
        List<String> offics = getOfficers();
        for (MPlayer player : Mafiacraft.getOnlinePlayers()) {
            if (offics.contains(player.getName())) {
                online.add(player);
            }
        }
        return online;
    }

    public String getOwnerId() {
        return "G" + id;
    }

    /**
     * Dispatches an invite to the given player as the specified player.
     *
     * @param inviter
     * @param invited
     * @return
     */
    public boolean dispatchInvite(MPlayer inviter, MPlayer invited) {
        Government them = invited.getGovernment();
        if (them != null && them.equals(this)) {
            return false;
        }

        invited.getSessionStore().setData("gov-inv", getId());

        invited.sendMessage(MsgColor.INFO + "The " + type.getName() + " " + name + " has invited you to join their ranks.");
        invited.sendMessage(MsgColor.INFO + "Type " + MsgColor.INFO_HILIGHT + "/" + type.getLocale("command") + " accept" + MsgColor.INFO + " to join.");

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This only applies to the land directly owned by the government, not
     * division land.</p>
     *
     * @param chunk
     * @return
     */
    public boolean canBeClaimed(Chunk chunk, LandOwner futureOwner) {
        return getPower() >= getLand();
    }

    /**
     * Returns true if the government is able to retain all of its land,
     * including division land.
     *
     * @return
     */
    public boolean canRetainAllLand() {
        return getPower() >= getTotalLand();
    }

    public boolean canClaimMoreLand() {
        return getMaxGovernmentLand() < getLand();
    }

    /**
     * Gets the maximum amount of land the government can own.
     *
     * @return
     */
    public int getMaxGovernmentLand() {
        return getTotalLand() >> 4;
    }

    public OwnerType getOwnerType() {
        return OwnerType.GOVERNMENT;
    }

    /**
     * {@inheritDoc}
     */
    public int getLand() {
        return land;
    }

    /**
     * {@inheritDoc}
     */
    public Government setLand(int amt) {
        this.land = amt;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Government incLand() {
        land++;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Government decLand() {
        land--;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Government claim(Chunk chunk) {
        District district = Mafiacraft.getDistrict(chunk);
        district.setOwner(chunk, this);
        incLand();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Government unclaim(Chunk chunk) {
        District district = Mafiacraft.getDistrict(chunk);
        district.setOwner(chunk, null);
        decLand();
        return this;
    }

    /**
     * Gets the HQ of the government.
     *
     * @return
     */
    public Location getHq() {
        return hq;
    }

    /**
     * Gets all council members as MPlayers. Potentially expensive if you're not
     * careful!
     *
     * @return
     */
    public List<MPlayer> getCouncilMembersAsMPlayers() {
        List<MPlayer> members = new ArrayList<MPlayer>();
        for (String player : getCouncilMembers()) {
            MPlayer mp = Mafiacraft.getPlayer(player);
            members.add(mp);
        }
        return members;
    }

    public boolean canHaveMoreDivisions() {
        for (Division division : getDivisions()) {
            if (division.getMemberCount() < 5) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the prefix of this government shown in chat.
     *
     * @return
     */
    public String getChatPrefix() {
        return getType().getColor() + "[" + getName() + "]";
    }

    /**
     * Broadcasts a message to the government.
     *
     * @param message
     */
    public void broadcastMessage(String message) {
        message = MsgColor.INFO_GOV + message;

        for (MPlayer player : getOnlineMembers()) {
            player.sendMessage(message);
        }
    }

    public String getEntryMessage() {
        return getName();
    }

    ////////////
    // SERIALIZATION
    ////////////
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id", getId());
        data.put("name", getName());
        data.put("type", getType().getName());
        data.put("land", getLand());
        data.put("hq", LocationSerializer.serializeFull(getHq()));

        data.put("leader", getLeader());
        data.put("vleader", getViceLeader());
        data.put("officers", getOfficers());
        data.put("affiliates", getAffiliates());

        return data;
    }

    /**
     * Deserializes a Government object.
     *
     * @param data The data in Map form.
     * @return The deserialized Government.
     */
    public static Government deserialize(Map<String, Object> data) {
        int id = 0;
        String strId = data.get("id").toString();
        try {
            id = Integer.parseInt(strId);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid number encountered when deserializing a government!", ex);
        }

        Government gov = new Government(id);

        String name = data.get("name").toString();

        String typeStr = data.get("type").toString();
        GovType type = GovType.fromString(typeStr);
        if (type == null) {
            MLogger.log(Level.SEVERE, "Invalid GovType encountered when loading a government: '" + typeStr + "'!");
        }

        Map<String, Object> hqM = (Map<String, Object>) data.get("hq");
        Location hq = LocationSerializer.deserializeFull(hqM);

        String landS = data.get("land").toString();
        int land = 0;
        try {
            land = Integer.parseInt(landS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid land amount encountered when loading a government: '" + landS + "'!");
        }

        String leader = data.get("leader").toString();
        String viceLeader = data.get("vleader").toString();

        List<String> officerList = (List<String>) data.get("officers");
        Set<String> officers = new HashSet<String>(officerList);

        List<String> affiliateList = (List<String>) data.get("affiliates");
        Set<String> affiliates = new HashSet<String>(affiliateList);

        //Set info
        gov.setName(name).setType(type);
        gov.setHq(hq).setLand(land);

        //Set members
        gov.setLeader(leader).setViceLeader(viceLeader);
        gov.setOfficers(officers).setAffiliates(affiliates);

        return gov;
    }

}
