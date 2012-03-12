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

import net.voxton.mafiacraft.chat.ChatHandler;
import net.voxton.mafiacraft.cmd.Commands;
import net.voxton.mafiacraft.data.DataWorker;
import net.voxton.mafiacraft.geo.CityManager;
import net.voxton.mafiacraft.gov.GovernmentManager;
import net.voxton.mafiacraft.locale.LocaleManager;
import net.voxton.mafiacraft.player.PlayerManager;
import net.voxton.mafiacraft.vault.VaultHelper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Mafiacraft main plugin class.
 */
public class MafiacraftPlugin extends JavaPlugin {

    private ChatHandler chatHandler;

    private CityManager cityManager;

    private DataWorker dataWorker;

    private GovernmentManager governmentManager;

    private LocaleManager localeManager;

    private PlayerManager playerManager;

    private VaultHelper vaultHelper;

    @Override
    public void onDisable() {
        //Start
        MLogger.log("======== MAFIACRAFT DISABLE BEGIN =======");

        //Notify
        MLogger.log("Mafiacraft " + getVersionDetailed() + " disabling...");

        //Unload the Mafiacraft singleton from the API class.
        MLogger.log("Unloading singleton...");
        Mafiacraft.unloadMafiacraft();

        //Cancel our tasks, i.e. our scheduler.
        MLogger.log("Cancelling tasks...");
        Bukkit.getScheduler().cancelTasks(this);

        //Save data
        MLogger.log("Saving data...");
        dataWorker.saveAll();

        //Log
        MLogger.log("========== MAFIACRAFT DISABLED ==========");
    }

    @Override
    public void onEnable() {
        //Setup the helper static class
        Mafiacraft.setPlugin(this);

        //Greet
        MLogger.log("=========== MAFIACRAFT START ===========");

        //Notify
        MLogger.log("Mafiacraft " + getVersionDetailed() + " loading...");

        //Setup config
        MLogger.log("Loading configuration...");
        MConfig.bind(this);
        saveConfig();

        //Setup commands
        MLogger.log("Registering commands...");
        Commands.registerAll();

        //Initialize the listener
        MLogger.log("Registering events...");
        MListener l = new MListener(this);
        Bukkit.getPluginManager().registerEvents(l, this);

        //Initialize managers/handlers/helpers
        MLogger.log("Initializing chat...");
        chatHandler = new ChatHandler(this);

        MLogger.log("Initializing cities...");
        cityManager = new CityManager(this);

        MLogger.log("Initializing data...");
        dataWorker = new DataWorker(this);

        MLogger.log("Initializing governments...");
        governmentManager = new GovernmentManager(this);

        MLogger.log("Initializing locale manager...");
        localeManager = new LocaleManager();

        MLogger.log("Initializing players...");
        playerManager = new PlayerManager(this);

        MLogger.log("Hooking Vault...");
        vaultHelper = new VaultHelper(this);

        //Load data
        MLogger.log("Loading all data...");
        dataWorker.loadAll();

        //Log
        MLogger.log("========== MAFIACRAFT ENABLED ==========");
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public CityManager getCityManager() {
        return cityManager;
    }

    public DataWorker getDataWorker() {
        return dataWorker;
    }

    public GovernmentManager getGovernmentManager() {
        return governmentManager;
    }

    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public VaultHelper getVaultHelper() {
        return vaultHelper;
    }

    /**
     * Gets the version of the plugin.
     *
     * @return The version of the plugin.
     */
    public String getVersion() {
        return getDescription().getVersion();
    }

    /**
     * Gets the implementation version of the plugin.
     *
     * @return The implementation version of the plugin.
     */
    public String getImplementationVersion() {
        return MafiacraftPlugin.class.getPackage().getImplementationVersion();
    }

    /**
     * Gets the detailed version of the plugin.
     *
     * @return The detailed version.
     */
    public String getVersionDetailed() {
        return getVersion() + "-" + getImplementationVersion();
    }

}
