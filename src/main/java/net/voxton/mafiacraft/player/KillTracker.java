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
package net.voxton.mafiacraft.player;

import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.MafiacraftCore;
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

    private final MafiacraftCore mc;

    public KillTracker(MafiacraftCore mc) {
        this.mc = mc;
        this.killFilePath = new File(mc.getDataFolder().toString()
                + File.separator + "kills.yml");
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
                MLogger.log(Level.SEVERE, "Could not create the kill file!", ex);
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
            MLogger.log(Level.SEVERE, "Could not save the kills!", ex);
        }
    }

}