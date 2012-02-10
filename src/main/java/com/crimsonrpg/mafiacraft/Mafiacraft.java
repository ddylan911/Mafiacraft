/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

import com.crimsonrpg.mafiacraft.geo.DistrictManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Mafiacraft main class
 */
public class Mafiacraft extends JavaPlugin {
    private static Mafiacraft instance;

    private static Logger LOGGER = Logger.getLogger("Minecraft");
    
    private DistrictManager districtManager;

    public Mafiacraft() {
        instance = this;
    }

    public void onDisable() {
        log("Mafiacraft disabled.");
    }

    public void onEnable() {
        log("Mafiacraft enabled.");
    }

    public DistrictManager getDistrictManager() {
        return districtManager;
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
