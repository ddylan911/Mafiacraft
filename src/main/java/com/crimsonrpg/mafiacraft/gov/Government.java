/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Chunk;

/**
 *
 * @author simplyianm
 */
public class Government implements LandOwner {

    private final int id;
    private String name;
    private String chatTag;
    private GovType type;
    private String leader;
    private String viceLeader;
    private List<Division> divisions;
    private List<String> officers;
    private List<String> affiliates;

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

                case AFFILIATE:
                    members.addAll(members);
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
        ArrayList<MPlayer> members = new ArrayList<MPlayer>();
        for (MPlayer player : MafiacraftPlugin.getInstance().getPlayerManager().getPlayerList()) {
            if (isMember(player)) {
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

        if (!removeMember(player)) {
            return false;
        }

        if (!canHaveMore(position)) {
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
     * Adds a member to this government as an affiliate.
     * 
     * @param player
     * @return 
     */
    public Government addMember(String player) {
        affiliates.add(player);
        return this;
    }

    /**
     * Adds a member to this government.
     * 
     * @param player
     * @param position
     * @return True if the operation was allowed.
     */
    public Government addMember(MPlayer player) {
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
}
