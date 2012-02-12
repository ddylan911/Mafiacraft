/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import com.crimsonrpg.mafiacraft.player.MPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author simplyianm
 */
public class Division implements LandOwner {
    private int id;

    private Government government;

    private String name;

    private String description;

    private String manager;

    private List<String> workers = new ArrayList<String>();

    public Division(int id, Government government) {
        this.id = id;
        this.government = government;
    }

    public boolean canBuild(MPlayer player, Chunk chunk) {
        return true;
    }

    public String getName() {
        return name;
    }

    public String getOwnerName() {
        return name + " of " + government.getName();
    }

    public String getOwnerId() {
        return "D-" + government.getId() + "-" + id;
    }

    public Government getGovernment() {
        return government;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Division addWorker(String player) {
        workers.add(player);
        return this;
    }

    public Division addWorker(MPlayer player) {
        return addWorker(player.getName());
    }

    public Position getPosition(String player) {
        if (isWorker(player)) {
            return Position.WORKER;
        }

        if (isManager(player)) {
            return Position.MANAGER;
        }

        return Position.NONE;
    }

    /**
     * Removes a player from the division.
     * 
     * @param player
     * @return True if the operation was allowed.
     */
    public boolean remove(String player) {
        switch (getPosition(player)) {
            case WORKER:
                workers.remove(player);
                break;

            case MANAGER:
                return false;
        }
        return true;
    }

    public List<String> getWorkers() {
        return new ArrayList<String>(workers);
    }

    public boolean isWorker(String player) {
        return workers.contains(player);
    }

    public boolean isWorker(MPlayer player) {
        return isWorker(player.getName());
    }

    public boolean isManager(String player) {
        return manager.equalsIgnoreCase(player);
    }

    public boolean isManager(MPlayer player) {
        return isManager(player.getName());
    }

    public boolean isMember(String player) {
        return isWorker(player) || isManager(player);
    }

    public Division save(ConfigurationSection dest) {
        dest.set("name", name);
        dest.set("desc", description);
        dest.set("members.manager", manager);
        dest.set("members.workers", workers);
        return this;
    }

}