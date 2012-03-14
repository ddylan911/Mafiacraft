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
import net.voxton.mafiacraft.city.CityManager;
import net.voxton.mafiacraft.geo.MWorld;
import net.voxton.mafiacraft.city.District;
import net.voxton.mafiacraft.city.LandOwner;
import net.voxton.mafiacraft.gov.GovernmentManager;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.PlayerManager;
import java.util.List;
import net.voxton.mafiacraft.action.ConsolePerformer;
import net.voxton.mafiacraft.data.DataWorker;
import net.voxton.mafiacraft.impl.MafiacraftImpl;
import net.voxton.mafiacraft.locale.Locale;
import net.voxton.mafiacraft.locale.LocaleManager;

/**
 * Mafiacraft API accessor static class.
 */
public class Mafiacraft {

    private static MafiacraftCore core;

    public static void setCore(MafiacraftCore mcp) {
        if (Mafiacraft.core == null) {
            Mafiacraft.core = mcp;
        }
    }

    /**
     * Gets the singleton instance of the Mafiacraft core.
     *
     * @return
     */
    public static MafiacraftCore getCore() {
        return core;
    }

    /**
     * Unloads Mafiacraft. Used for handling reloads.
     */
    static void unloadMafiacraft() {
        core = null;
    }

    /**
     * Returns a list of all MPlayers currently online the server.
     *
     * @return
     */
    public static List<MPlayer> getOnlinePlayers() {
        return getCore().getPlayerManager().getOnlinePlayers();
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
        return getCore().getChatHandler();
    }

    /**
     * Gets the city manager.
     *
     * @return
     */
    public static CityManager getCityManager() {
        return getCore().getCityManager();
    }
    
    /**
     * Gets the console performer.
     * 
     * @return The console performer.
     */
    public static ConsolePerformer getConsolePerformer() {
        return getCore().getConsolePerformer();
    }

    /**
     * Gets the data worker.
     *
     * @return The data worker.
     */
    public static DataWorker getDataWorker() {
        return getCore().getDataWorker();
    }

    /**
     * Gets the government manager.
     *
     * @return The government manager.
     */
    public static GovernmentManager getGovernmentManager() {
        return getCore().getGovernmentManager();
    }

    /**
     * Gets the locale manager.
     * 
     * @return The locale manager.
     */
    public static LocaleManager getLocaleManager() {
        return getCore().getLocaleManager();
    }

    /**
     * Gets the player manager.
     *
     * @return
     */
    public static PlayerManager getPlayerManager() {
        return getCore().getPlayerManager();
    }

    /**
     * Gets the Mafiacraft implementation.
     * 
     * @return The implementation.
     */
    public static MafiacraftImpl getImpl() {
        return getCore().getImpl();
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

    /**
     * Gets a CityWorld from its name.
     * 
     * @param worldString The string name of the CityWorld.
     * @return The CityWorld.
     */
    public static MWorld getWorld(String worldString) {
        return getCityManager().getWorld(worldString);
    }

}
