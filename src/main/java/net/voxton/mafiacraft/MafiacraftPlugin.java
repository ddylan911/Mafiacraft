/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft;

import net.voxton.mafiacraft.chat.ChatHandler;
import net.voxton.mafiacraft.cmd.Commands;
import net.voxton.mafiacraft.data.DataWorker;
import net.voxton.mafiacraft.geo.CityManager;
import net.voxton.mafiacraft.gov.GovernmentManager;
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

    private PlayerManager playerManager;

    private VaultHelper vaultHelper;

    @Override
    public void onDisable() {
        //Unload the Mafiacraft singleton from the API class.
        Mafiacraft.unloadMafiacraft();

        //Cancel our tasks, i.e. our scheduler.
        Bukkit.getScheduler().cancelTasks(this);

        //Save data
        dataWorker.saveAll();

        //Log
        MLogger.log("Mafiacraft disabled.");
    }

    @Override
    public void onEnable() {
        //Setup the helper static class
        Mafiacraft.setPlugin(this);

        //Setup config
        MConfig.bind(this);
        saveConfig();

        //Setup commands
        Commands.registerAll();

        //Initialize the listener
        MListener l = new MListener(this);
        Bukkit.getPluginManager().registerEvents(l, this);

        //Initialize managers/handlers/helpers
        chatHandler = new ChatHandler(this);
        cityManager = new CityManager(this);
        dataWorker = new DataWorker(this);
        governmentManager = new GovernmentManager(this);
        playerManager = new PlayerManager(this);
        vaultHelper = new VaultHelper(this);

        //Load data
        dataWorker.loadAll();

        //Log
        MLogger.log("Mafiacraft enabled.");
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

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public VaultHelper getVaultHelper() {
        return vaultHelper;
    }

}
