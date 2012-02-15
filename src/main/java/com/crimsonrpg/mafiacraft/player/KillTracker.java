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

    public int getKillScore(MPlayer player) {
        return getkillFile().getInt("kills." + player.getName().toLowerCase(), 0);
    }

    public void setKillScore(MPlayer player, int score) {
        getkillFile().set("kills." + player.getName().toLowerCase(), score);
        save();
    }

    public void incScore(MPlayer player) {
        addScore(player, 1);
        save();
    }

    public void decScore(MPlayer player) {
        subScore(player, 1);
        save();
    }

    public KillTracker addScore(MPlayer player, int amt) {
        getkillFile().set("kills." + player.getName().toLowerCase(), getKillScore(player) + amt);
        save();
        return this;
    }

    public KillTracker subScore(MPlayer player, int amt) {
        getkillFile().set("kills." + player.getName().toLowerCase(), getKillScore(player) - amt);
        save();
        return this;
    }

    public YamlConfiguration load() {
        File kf = new File(MafiacraftPlugin.getInstance().getDataFolder().toString() + File.separator + "kills.yml");

        if (!kf.exists()) {
            try {
                kf.createNewFile();
            } catch (IOException ex) {
                MafiacraftPlugin.getInstance().log(Level.SEVERE, "Could not create the kill file!", ex);
            }
        }

        killFile = YamlConfiguration.loadConfiguration(kf);
        return killFile;
    }

    public YamlConfiguration getkillFile() {
        return killFile;
    }

    public void save() {
        File kf = new File(MafiacraftPlugin.getInstance().getDataFolder().toString() + File.separator + "kills.yml");
        try {
            killFile.save(kf);
        } catch (IOException ex) {
            MafiacraftPlugin.getInstance().log(Level.SEVERE, "Could not save the kills!", ex);
        }
    }
}