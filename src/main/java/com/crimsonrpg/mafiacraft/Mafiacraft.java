/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

import com.crimsonrpg.mafiacraft.chat.ChatHandler;
import com.crimsonrpg.mafiacraft.geo.CityManager;
import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.geo.LandOwner;
import com.crimsonrpg.mafiacraft.gov.GovernmentManager;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.PlayerManager;
import com.crimsonrpg.mafiacraft.vault.VaultHelper;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 * Mafiacraft API accessor static class.
 */
public class Mafiacraft {
    private static MafiacraftPlugin plugin;

    public static void setPlugin(MafiacraftPlugin mcp) {
        if (Mafiacraft.plugin == null) {
            Mafiacraft.plugin = mcp;
        }
    }

    public static MafiacraftPlugin getPlugin() {
        return plugin;
    }

    /**
     * Gets an MPlayer from a Player.
     *
     * @param player
     * @return
     */
    public static MPlayer getPlayer(Player player) {
        return getPlugin().getPlayerManager().getPlayer(player);
    }

    /**
     * Returns a list of all MPlayers currently online the server.
     *
     * @return
     */
    public static List<MPlayer> getOnlinePlayers() {
        return getPlugin().getPlayerManager().getOnlinePlayers();
    }

    /**
     * Gets the chat handler.
     *
     * @return
     */
    public static ChatHandler getChatHandler() {
        return getPlugin().getChatHandler();
    }

    /**
     * Gets the city manager.
     *
     * @return
     */
    public static CityManager getCityManager() {
        return getPlugin().getCityManager();
    }

    /**
     * Gets the government manager.
     *
     * @return
     */
    public static GovernmentManager getGovernmentManager() {
        return getPlugin().getGovernmentManager();
    }

    /**
     * Gets the player manager.
     *
     * @return
     */
    public static PlayerManager getPlayerManager() {
        return getPlugin().getPlayerManager();
    }

    /**
     * Gets the vault helper.
     *
     * @return
     */
    public static VaultHelper getVaultHelper() {
        return getPlugin().getVaultHelper();
    }

    /**
     * Gets the district associated with the given chunk.
     *
     * @param chunk
     * @return
     */
    public static District getDistrict(Chunk chunk) {
        return getCityManager().getDistrict(chunk);
    }

    /**
     * Gets the owner of the given section of land.
     *
     * @param section
     * @return
     */
    public static LandOwner getSectionOwner(Chunk section) {
        return getCityManager().getSectionOwner(section);
    }

    /**
     * Gets the LandOwner from the given String id.
     *
     * @param id
     * @return
     */
    public static LandOwner getLandOwner(String id) {
        return getCityManager().getLandOwner(id);
    }

    /**
     * Gets a player based on their string name, online or not.
     *
     * @param player
     * @return
     */
    public static MPlayer getPlayer(String player) {
        return getPlayerManager().getPlayer(player);
    }

}
