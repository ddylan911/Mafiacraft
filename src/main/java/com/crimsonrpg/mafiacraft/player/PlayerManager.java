/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.MLogger;
import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.classes.UtilityClass;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
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
            MLogger.log(Level.SEVERE + "Execution exception for getting a player!");
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

        YamlConfiguration pf = getPlayerYml(player);
        mplayer.load(pf).save(pf);

        //Just in case defaults were created.
        savePlayerYml(player, pf);

        return mplayer;
    }

    /**
     * Gets the folder where player info is kept. s
     *
     * @return
     */
    private File getPlayerFolder() {
        File folder = new File(mc.getDataFolder().getPath() + File.separator + "players" + File.separator);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    /**
     * Gets a player's file.
     *
     * @param player
     * @return
     */
    private File getPlayerFile(String player) {
        File file = new File(getPlayerFolder().getPath() + File.separator + player + ".yml");

        try {
            file.createNewFile();
        } catch (IOException ex) {
            MLogger.log(player + "'s file could not be created!");
        }

        return file;
    }

    /**
     * Gets a player's YML file.
     *
     * @param player
     * @return
     */
    private YamlConfiguration getPlayerYml(String player) {
        return YamlConfiguration.loadConfiguration(getPlayerFile(player));
    }

    /**
     * Gets a player's YML file.
     *
     * @param player
     * @return
     */
    private YamlConfiguration getPlayerYml(OfflinePlayer player) {
        return getPlayerYml(player.getName());
    }

    /**
     * Saves a player's YAML file.
     *
     * @param player
     * @param yml
     * @return
     */
    private boolean savePlayerYml(String player, YamlConfiguration yml) {
        try {
            yml.save(getPlayerFile(player));
            return true;
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE, "The file for the player " + player + " could not be saved!");
        }
        return false;
    }

    /**
     * Saves a player's YML file.
     *
     * @param player
     * @param yml
     * @return
     */
    private boolean savePlayerYml(OfflinePlayer player, YamlConfiguration yml) {
        return savePlayerYml(player.getName(), yml);
    }

    /**
     * Gets a player that is currently online.
     *
     * @param target The name of the player.
     * @return
     */
    public MPlayer getOnlinePlayer(String target) {
        Player player = Bukkit.getPlayer(target);
        if (player == null) {
            return null;
        }
        return getPlayer(player);
    }

}
