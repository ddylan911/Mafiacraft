/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft;

import net.voxton.mafiacraft.chat.ChatHandler;
import net.voxton.mafiacraft.cmd.Commands;
import net.voxton.mafiacraft.geo.CityManager;
import net.voxton.mafiacraft.gov.GovernmentManager;
import net.voxton.mafiacraft.player.PlayerManager;
import net.voxton.mafiacraft.vault.VaultHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Mafiacraft main class
 */
public class MafiacraftPlugin extends JavaPlugin {
    private static MafiacraftPlugin instance;

    private ChatHandler chatHandler;

    private CityManager cityManager;

    private GovernmentManager governmentManager;

    private PlayerManager playerManager;

    private VaultHelper vaultHelper;

    public MafiacraftPlugin() {
        instance = this;
    }

    public void onDisable() {
        MLogger.log("Mafiacraft disabled.");
    }

    public void onEnable() {
        //Setup the helper static class
        Mafiacraft.setPlugin(this);

        //Setup config
        MConfig.bind(this);
        saveConfig();

        //Setup commands
        Commands.registerAll(this);

        //Initialize the listener
        MListener l = new MListener(this);
        Bukkit.getPluginManager().registerEvents(l, this);

        //Initialize managers/handlers/helpers
        chatHandler = new ChatHandler(this);
        cityManager = new CityManager(this);
        governmentManager = new GovernmentManager(this);
        playerManager = new PlayerManager(this);
        vaultHelper = new VaultHelper(this);

        MLogger.log("Mafiacraft enabled.");
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public CityManager getCityManager() {
        return cityManager;
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

    public static MafiacraftPlugin getInstance() {
        return instance;
    }

}
