/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class PlayerManager {
    private Cache<String, MPlayer> players;

    private final MafiacraftPlugin mc;

    private KillTracker killTracker;

    public PlayerManager(MafiacraftPlugin mc) {
        this.mc = mc;
        this.killTracker = new KillTracker(mc);

        buildCache();
    }

    /**
     * Creates the player cache.
     */
    private void buildCache() {
        players = CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(10, TimeUnit.MINUTES).build(
                new CacheLoader<String, MPlayer>() {
                    @Override
                    public MPlayer load(String key) throws Exception {
                        return loadPlayer(key);
                    }

                });
    }

    /**
     * Gets the KillTracker.
     *
     * @return
     */
    public KillTracker getKillTracker() {
        return killTracker;
    }

    /**
     * Gets a list of all players.
     *
     * @return
     */
    public List<MPlayer> getOnlinePlayers() {
        List<MPlayer> players = new ArrayList<MPlayer>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(getPlayer(player));
        }
        return players;
    }

    /**
     * Gets a player by their name.
     * 
     * @param name
     * @return 
     */
    public MPlayer getPlayer(String name) {
        try {
            return players.get(name);
        } catch (ExecutionException ex) {
            MafiacraftPlugin.log(Level.SEVERE + "Execution exception for getting a player!");
        }
        return null;
    }

    /**
     * Gets a player from a Bukkit player.
     *
     * @param player
     * @return
     */
    public MPlayer getPlayer(Player player) {
        return getPlayer(player.getName());
    }

    /**
     * Loads a player.
     *
     * @param player
     * @return
     */
    private MPlayer loadPlayer(String player) {
        return loadPlayer(Bukkit.getOfflinePlayer(player));
    }

    /**
     * Loads a player.
     *
     * @param player
     * @return
     */
    private MPlayer loadPlayer(OfflinePlayer player) {
        MPlayer mplayer = new MPlayer(player);
        //TODO: actually load the player's data
        return mplayer;
    }

}
