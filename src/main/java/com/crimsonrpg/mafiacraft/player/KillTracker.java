/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Tracks kills
 */
public class KillTracker {
    private YamlConfiguration killFile;

    private File killFilePath;

    private final MafiacraftPlugin mc;

    public KillTracker(MafiacraftPlugin mc) {
        this.mc = mc;
        this.killFilePath = new File(mc.getDataFolder().toString() + File.separator + "kills.yml");
    }

    public int getKillScore(MPlayer player) {
        return getKillFile().getInt("kills." + player.getName().toLowerCase(), 0);
    }

    public void setKillScore(MPlayer player, int score) {
        getKillFile().set("kills." + player.getName().toLowerCase(), score);
        save();
    }

    public void incScore(MPlayer player) {
        addScore(player, 1);
    }

    public void decScore(MPlayer player) {
        subScore(player, 1);
    }

    public KillTracker addScore(MPlayer player, int amt) {
        setKillScore(player, player.getKillScore() + amt);
        return this;
    }

    public KillTracker subScore(MPlayer player, int amt) {
        setKillScore(player, player.getKillScore() - amt);
        return this;
    }

    public YamlConfiguration load() {
        if (!killFilePath.exists()) {
            try {
                killFilePath.createNewFile();
            } catch (IOException ex) {
                MafiacraftPlugin.log(Level.SEVERE, "Could not create the kill file!", ex);
            }
        }
        killFile = YamlConfiguration.loadConfiguration(killFilePath);
        return killFile;
    }

    public YamlConfiguration getKillFile() {
        return killFile;
    }

    public void save() {
        try {
            killFile.save(killFilePath);
        } catch (IOException ex) {
            MafiacraftPlugin.log(Level.SEVERE, "Could not save the kills!", ex);
        }
    }

}