/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.player;

import com.google.common.cache.*;
import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.MafiacraftPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import net.voxton.mafiacraft.Mafiacraft;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
    private final MafiacraftPlugin mc;

    /**
     * The Kill tracker.
     */
    private KillTracker killTracker;

    /**
     * Constructor.
     * 
     * @param mc The MafiaCraft plugin.
     */
    public PlayerManager(MafiacraftPlugin mc) {
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
     * @return The MPlayer corresponding with the name.
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
     * @return The player that corresponds to the given Bukkit player.
     */
    public MPlayer getPlayer(Player player) {
        if (player == null) {
            return null;
        }
        return getPlayer(player.getName());
    }

    /**
     * Loads a player.
     *
     * @param player
     * @return The player loaded from the given player.
     */
    private MPlayer loadPlayer(String player) {
        return loadPlayer(Bukkit.getOfflinePlayer(player));
    }

    /**
     * Loads a player.
     *
     * @param player The player to load the MPlayer of.
     * @return The player loaded from the OfflinePlayer.
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
     * Gets a player's file.
     *
     * @param player
     * @return
     */
    private File getPlayerFile(String player) {
       return Mafiacraft.getSubFile("player", player + ".ymlÏ");
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
     * Saves the given MPlayer to its appropriate file.
     *
     * @param player The player to save.
     * @return True if the saving was successful, false otherwise.
     */
    public boolean savePlayer(MPlayer player) {
        YamlConfiguration yml = getPlayerYml(player.getBukkitEntity());
        player.save(yml);
        boolean result = savePlayerYml(player.getBukkitEntity(), yml);
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
            MLogger.log(Level.SEVERE, "The file for the player " + player + " could not be saved!", ex);
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
