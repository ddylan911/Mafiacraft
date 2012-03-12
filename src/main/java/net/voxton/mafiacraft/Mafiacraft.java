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
package net.voxton.mafiacraft;

import java.io.File;
import net.voxton.mafiacraft.chat.ChatHandler;
import net.voxton.mafiacraft.geo.CityManager;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.geo.LandOwner;
import net.voxton.mafiacraft.gov.GovernmentManager;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.PlayerManager;
import net.voxton.mafiacraft.vault.VaultHelper;
import java.util.List;
import net.voxton.mafiacraft.data.DataWorker;
import net.voxton.mafiacraft.locale.Locale;
import net.voxton.mafiacraft.locale.LocaleManager;
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

    /**
     * Gets the singleton instance of the Mafiacraft plugin.
     *
     * @return
     */
    public static MafiacraftPlugin getPlugin() {
        return plugin;
    }

    /**
     * Unloads Mafiacraft. Used for handling reloads.
     */
    static void unloadMafiacraft() {
        plugin = null;
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
     * Gets the specified subfolder of the plugin.
     *
     * @param name The name of the folder.
     * @return The subfolder specified.
     */
    public static File getSubFolder(String name) {
        return getDataWorker().getSubFolder(name);
    }

    /**
     * Gets a top-level file with the given name.
     *
     * @param name The name of the file.
     * @return The created file.
     */
    public static File getTopFile(String name) {
        return getDataWorker().getTopFile(name);
    }

    /**
     * Gets a second-level file with the given name and directory and creates it 
     * if it doesn't exist.
     *
     * @param dir The directory containing the file.
     * @param name The name of the file.
     * @return The file retrieved.
     */
    public static File getOrCreateSubFile(String dir, String name) {
        return getDataWorker().getOrCreateSubFile(dir, name);
    }

    /**
     * Gets a second-level file with the given name and directory.
     *
     * @param dir The directory containing the file.
     * @param name The name of the file.
     * @return The file retrieved.
     */
    public static File getSubFile(String dir, String name) {
        return getDataWorker().getSubFile(dir, name);
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
     * Gets the data worker.
     *
     * @return The data worker.
     */
    public static DataWorker getDataWorker() {
        return getPlugin().getDataWorker();
    }

    /**
     * Gets the government manager.
     *
     * @return The government manager.
     */
    public static GovernmentManager getGovernmentManager() {
        return getPlugin().getGovernmentManager();
    }

    /**
     * Gets the locale manager.
     * 
     * @return The locale manager.
     */
    public static LocaleManager getLocaleManager() {
        return getPlugin().getLocaleManager();
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
     * Gets the owner of the given chunk.
     *
     * @param last
     * @return
     */
    public static LandOwner getOwner(Chunk chunk) {
        return getCityManager().getSectionOwner(chunk);
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

    /**
     * Gets a player that is online.
     *
     * @param target
     * @return
     */
    public static MPlayer getOnlinePlayer(String target) {
        return getPlayerManager().getOnlinePlayer(target);
    }

    /**
     * Loads everything into memory.
     */
    public static void loadAll() {
        getDataWorker().loadAll();
    }

    /**
     * Saves all things possible to save.
     */
    public static void saveAll() {
        getDataWorker().saveAll();
    }

    /**
     * Gets a list of all districts.
     * 
     * @return A list of all districts.
     */
    public static List<District> getDistrictList() {
        return getCityManager().getDistrictList();
    }

    /**
     * Gets a list of all locales.
     * 
     * @return The list of all locales.
     */
    public static List<Locale> getLocales() {
        return getLocaleManager().getLocales();
    }
    
    /**
     * Gets the default locale of Mafiacraft.
     * 
     * @return The default locale.
     */
    public static Locale getDefaultLocale() {
        return getLocaleManager().getDefault();
    }

    /**
     * Gets a locale by name.
     * 
     * @param name The name of the locale.
     * @return The locale.
     */
    public static Locale getLocale(String name) {
        return getLocaleManager().getLocale(name);
    }
}
