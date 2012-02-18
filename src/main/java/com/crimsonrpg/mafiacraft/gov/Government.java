/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.geo.LandOwner;
import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.geo.LandPurchaser;
import com.crimsonrpg.mafiacraft.geo.OwnerType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import com.crimsonrpg.mafiacraft.vault.Transactable;
import java.util.*;
import java.util.Map.Entry;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * Represents a Government, a mafia or a police.
 */
public class Government extends Transactable implements LandPurchaser {
    private final int id;

    private String name;

    private String chatTag;

    private GovType type;

    private String leader;

    private String viceLeader;

    private List<Division> divisions;

    private Set<String> officers;

    private Set<String> affiliates;

    private Location hq;

    /**
     * Holds the land of the government. (Not divisions!)
     */
    private int land;

    /**
     * Holds the money of the government.
     */
    private double money;

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
     * Gets the power the government has over its land. For now, this is the
     * same as getMaxPower().
     *
     * <p>NOTE: In my opinion, it should be lessened based on other factors so
     * it's actually possible to claim other governments after wars or
     * something.</p>
     */
    public int getPower() {
        return getMaxPower();
    }

    /**
     * Gets the power of the government.
     *
     * <p>The power of a government is calculated by the integer of money
     * divided by 1024. Mafias should start out with 128 sections worth of
     * power, or 128 power, or 128 * 1024 dollars = $131,072 to start out. This
     * is better rounded to $150k.</p>
     *
     * @return
     */
    public int getMaxPower() {
        return ((int) Math.floor(money)) >> 10; //2 to the 10th, aka 1024
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

    /**
     * Gets the chat tag of this government.
     *
     * @return
     */
    public String getChatTag() {
        return chatTag;
    }

    /**
     * Sets the chat tag of this government.
     *
     * @param chatTag
     */
    public void setChatTag(String chatTag) {
        this.chatTag = chatTag;
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
        if (position.isDivision()) {
            switch (position) {
                case WORKER:
                    for (Division division : divisions) {
                        members.addAll(division.getWorkers());
                    }
                    break;

                case MANAGER:
                    for (Division division : divisions) {
                        members.add(division.getManager());
                    }
                    break;
            }
        } else {
            switch (position) {
                case LEADER:
                    members.add(leader);
                    break;

                case VICE_LEADER:
                    members.add(viceLeader);
                    break;

                case OFFICER:
                    members.addAll(officers);
                    break;

                case AFFILIATE:
                    members.addAll(affiliates);
                    break;
            }
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
        for (Division division : divisions) {
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
        return new ArrayList<Division>(divisions);
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
     * Sets a player's position.
     *
     * @param player
     * @param position
     * @return
     */
    public boolean setPosition(String player, Position position) {
        if (position.isDivision()) {
            return false;
        }

        if (!canHaveMore(position)) {
            return false;
        }

        if (!removeMember(player)) {
            return false;
        }

        switch (position) {
            case AFFILIATE:
                addMember(player);
                break;

            case OFFICER:
                officers.add(player);
                break;

            case VICE_LEADER:
                String oldV = getViceLeader();
                viceLeader = player;
                addMember(oldV);
                break;

            case LEADER:
                String oldL = getLeader();
                leader = player;
                addMember(oldL);
                break;
        }
        return true;
    }

    /**
     * Sets a player's position to the one specified.
     *
     * @param player
     * @param position
     * @return
     */
    public boolean setPosition(MPlayer player, Position position) {
        return setPosition(player.getName(), position);
    }

    /**
     * Removes a member from this government.
     *
     * @param player
     * @return
     */
    public boolean removeMember(String player) {
        Position position = getPosition(player);
        if (position.equals(Position.NONE)) {
            return false;
        }

        if (position.isDivision()) {
            getDivision(player).remove(player);
        } else {
            switch (position) {
                case LEADER:
                    return false;

                case VICE_LEADER:
                    return false;

                case OFFICER:
                    if (canHaveLess(Position.OFFICER)) {
                        officers.remove(player);
                    }
                    break;

                case AFFILIATE:
                    affiliates.remove(player);
                    break;
            }
        }
        return true;
    }

    /**
     * Removes a member from this government.
     *
     * @param player
     */
    public boolean removeMember(MPlayer player) {
        return removeMember(player.getName());
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
     * Gets a list of all affiliates.
     *
     * @return
     */
    public List<String> getAffiliates() {
        return new ArrayList<String>(affiliates);
    }

    /**
     * Adds a member to this government as an affiliate.
     *
     * @param player
     * @return True if the operation was allowed.
     */
    public boolean addMember(String player) {
        return affiliates.add(player);
    }

    /**
     * Adds a member to this government.
     *
     * @param player
     * @param position
     * @return True if the operation was allowed.
     */
    public boolean addMember(MPlayer player) {
        return addMember(player.getName());
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
        return "G-" + id;
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
        if (them.equals(this)) {
            return false;
        }

        invited.sendMessage(MsgColor.INFO + "The " + type.getName() + " " + name + " has invited you to their ranks.");
        invited.sendMessage(MsgColor.INFO + "Type /" + type.getLocale("command") + " accept to join.");

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

    public Location getHq() {
        return hq;
    }

    public Government setHq(Location hq) {
        this.hq = hq;
        return this;
    }

}
