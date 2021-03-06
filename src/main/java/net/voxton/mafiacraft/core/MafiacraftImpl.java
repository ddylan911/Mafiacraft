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
package net.voxton.mafiacraft.core;

import java.io.File;
import java.io.InputStream;
import java.util.Set;
import java.util.logging.Logger;
import net.voxton.mafiacraft.core.config.MafiacraftConfig;
import net.voxton.mafiacraft.core.geo.MPoint;
import net.voxton.mafiacraft.core.geo.MWorld;
import net.voxton.mafiacraft.core.player.MPlayer;

/**
 * Represents an implementation of Mafiacraft.
 */
public interface MafiacraftImpl {

    public String getFullName();

    public void saveConfig();

    public void cancelTasks();

    public void setupCommands();

    public void setupEconomy();

    public void registerEvents();

    public File getDataFolder();

    public Logger getLogger();

    public MWorld getWorld(String name);

    public MafiacraftConfig getMafiacraftConfig();

    public MPoint getPoint(MPlayer player);

    public int scheduleRepeatingTask(Runnable runnable, long interval);

    public int scheduleDelayedTask(Runnable runnable, long interval);

    public void cancelTask(int id);

    public void teleportPlayer(MPlayer player, MPoint point);

    public boolean isOnline(MPlayer player);

    public void sendMessage(MPlayer player, String message);

    public boolean hasPermission(MPlayer player, String permission);

    public Set<MPlayer> getOnlinePlayers();

    public String matchPlayerName(String name);

    public double getMoney(MPlayer player);

    public double setMoney(MPlayer player, double amt);

    public InputStream getJarResource(String path);

    public String getServerVersion();

}
