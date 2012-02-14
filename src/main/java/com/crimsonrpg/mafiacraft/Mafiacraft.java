/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

import com.crimsonrpg.mafiacraft.chat.ChatHandler;
import com.crimsonrpg.mafiacraft.cmd.Commands;
import com.crimsonrpg.mafiacraft.geo.CityManager;
import com.crimsonrpg.mafiacraft.gov.GovernmentManager;
import com.crimsonrpg.mafiacraft.player.PlayerManager;
import com.crimsonrpg.mafiacraft.vault.VaultHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Mafiacraft main class
 */
public class Mafiacraft extends JavaPlugin {
    private static Mafiacraft instance;

    private static final Logger LOGGER = Logger.getLogger("Minecraft");

    private ChatHandler chatHandler;

    private CityManager cityManager;

    private GovernmentManager governmentManager;

    private PlayerManager playerManager;

    private VaultHelper vaultHelper;

    public Mafiacraft() {
        instance = this;
    }

    public void onDisable() {
        log("Mafiacraft disabled.");
    }

    public void onEnable() {
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

        log("Mafiacraft enabled.");
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

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static void log(Level level, String msg) {
        msg = "[MC] " + msg;
        LOGGER.log(level, msg);
    }

    public static void log(Level level, String msg, Throwable thrown) {
        msg = "[MC] " + msg;
        LOGGER.log(level, msg, thrown);
    }

    public static void logVerbose(String message) {
        logVerbose(message, 1);
    }

    public static void logVerbose(String message, int level) {
        log("[V" + level + "] " + message);
    }

    public static Mafiacraft getInstance() {
        return instance;
    }

}
