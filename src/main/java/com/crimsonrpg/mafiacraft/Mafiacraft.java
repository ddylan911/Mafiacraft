/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

import com.crimsonrpg.mafiacraft.geo.CityManager;
import com.crimsonrpg.mafiacraft.gov.GovernmentManager;
import com.crimsonrpg.mafiacraft.player.PlayerManager;
import com.crimsonrpg.mafiacraft.player.SessionManager;
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

    private static Logger LOGGER = Logger.getLogger("Minecraft");

    private CityManager cityManager;

    private GovernmentManager governmentManager;

    private PlayerManager playerManager;

    private SessionManager sessionManager;

    private VaultHelper vaultHelper;

    public Mafiacraft() {
        instance = this;
    }

    public void onDisable() {
        log("Mafiacraft disabled.");
    }

    public void onEnable() {
        //Initialize the listener
        MListener l = new MListener(this);
        Bukkit.getPluginManager().registerEvents(l, this);

        //Initialize managers
        cityManager = new CityManager(this);
        playerManager = new PlayerManager(this);
        sessionManager = new SessionManager(this);

        log("Mafiacraft enabled.");
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

    public SessionManager getSessionManager() {
        return sessionManager;
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

    public static Mafiacraft getInstance() {
        return instance;
    }

}
