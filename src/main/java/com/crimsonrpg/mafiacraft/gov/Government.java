/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private Map<Position, List<String>> positions = new HashMap<Position, List<String>>();

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

    /**
     * Gets the leader of the government.
     * 
     * @return 
     */
    public String getLeader() {
        List<String> members = getMembers(Position.LEADER);
        if (members == null) {
            return null;
        }

        if (members.isEmpty()) {
            return null;
        }

        return members.get(0);
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
     * Gets the members of a specified position.
     * 
     * @param position
     * @return 
     */
    public List<String> getMembers(Position position) {
        List<String> members = positions.get(position);
        if (members == null) {
            members = new ArrayList<String>();
            positions.put(position, members);
        }
        return members;
    }

    /**
     * Gets a list of all online members of this government.
     * 
     * @return 
     */
    public List<MPlayer> getOnlineMembers() {
        ArrayList<MPlayer> members = new ArrayList<MPlayer>();
        List<String> allMembers = getMembers();
        for (MPlayer player : Mafiacraft.getInstance().getPlayerManager().getPlayerList()) {
            if (allMembers.contains(player.getPlayer().getName())) {
                members.add(player);
            }
        }
        return members;
    }

}
