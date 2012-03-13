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
package net.voxton.mafiacraft.player;

import com.google.common.cache.*;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.voxton.mafiacraft.logging.MLogger;
import net.voxton.mafiacraft.MafiacraftCore;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.MPoint;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Manages MPlayer objects.
 */
public class PlayerManager {

    /**
     * Cache which stores all players.
     */
    private Cache<String, MPlayer> players;

    /**
     * Hook to the plugin.
     */
    private final MafiacraftCore mc;

    /**
     * The Kill tracker.
     */
    private KillTracker killTracker;

    /**
     * Holds people teleporting, value as the task id.
     */
    private TObjectIntMap<MPlayer> teleporting =
            new TObjectIntHashMap<MPlayer>();

    /**
     * Constructor.
     *
     * @param mc The MafiaCraft plugin.
     */
    public PlayerManager(MafiacraftCore mc) {
        this.mc = mc;
        this.killTracker = new KillTracker(mc);

        buildCache();
    }

    /**
     * Creates the player cache.
     */
    private void buildCache() {
        CacheBuilder builder = CacheBuilder.newBuilder();

        builder.maximumSize(10000).expireAfterWrite(10, TimeUnit.MINUTES);

        builder.removalListener(new RemovalListener<String, MPlayer>() {

            @Override
            public void onRemoval(RemovalNotification<String, MPlayer> rn) {
                savePlayer(rn.getValue());
            }

        });

        players = builder.build(
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
     * Saves all cached players to their appropriate files.
     *
     * @return This player manager.
     */
    public PlayerManager saveAll() {
        for (MPlayer player : players.asMap().values()) {
            savePlayer(player);
        }
        return this;
    }

    /**
     * Gets a list of all players.
     *
     * @return
     */
    public List<MPlayer> getOnlinePlayers() {
        return Mafiacraft.getImpl().getOnlinePlayers();
    }

    /**
     * Gets a player by their name.
     *
     * @param name
     * @return The MPlayer corresponding with the name.
     */
    public MPlayer getPlayer(String name) {
        name = Mafiacraft.getImpl().matchPlayerName(name);
        try {
            return players.get(name);
        } catch (ExecutionException ex) {
            MLogger.log(Level.SEVERE
                    + "Execution exception for getting a player!");
        }
        return null;
    }

    /**
     * Loads a player.
     *
     * @param player
     * @return The player loaded from the given player.
     */
    private MPlayer loadPlayer(String player) {
        MPlayer mplayer = new MPlayer(player);

        YamlConfiguration pf = getPlayerYml(player);
        mplayer.load(pf).save(pf);

        //We will not register the alias in the LandOwner/id map. It will be loaded.

        //Just in case defaults were created.
        savePlayerYml(player, pf);

        return mplayer;
    }

    /**
     * Gets a player's file.
     *
     * @param player
     * @return
     */
    private File getPlayerFile(String player) {
        return Mafiacraft.getOrCreateSubFile("player", player + ".yml");
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
     * Saves the given MPlayer to its appropriate file.
     *
     * @param player The player to save.
     * @return True if the saving was successful, false otherwise.
     */
    public boolean savePlayer(MPlayer player) {
        YamlConfiguration yml = getPlayerYml(player.getName());
        player.save(yml);
        boolean result = savePlayerYml(player.getName(), yml);
        return result;
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
            MLogger.log(Level.SEVERE, "The file for the player " + player
                    + " could not be saved!", ex);
        }
        return false;
    }

    /**
     * Gets a player that is currently online.
     *
     * @param target The name of the player.
     * @return Null if the player is not online.
     */
    public MPlayer getOnlinePlayer(String target) {
        MPlayer player = getPlayer(target);
        return (player.isOnline()) ? player : null;
    }

    public PlayerManager startTeleport(MPlayer player, int duration,
            MPoint point) {
        if (hasTeleport(player)) {
            cancelTeleport(player);
        }

        TeleportCountdown tc = new TeleportCountdown(player, duration, point);
        teleporting.put(player, Mafiacraft.getImpl().scheduleRepeatingTask(tc,
                20L));
        return this;
    }

    public PlayerManager cancelTeleport(MPlayer player) {
        int task = teleporting.get(player);
        if (task > 0) {
            Mafiacraft.getImpl().cancelTask(task);
        }
        return this;
    }

    public boolean hasTeleport(MPlayer player) {
        return teleporting.containsKey(player);
    }

}
