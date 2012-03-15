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
package net.voxton.mafiacraft.spout;

import java.util.Set;
import net.voxton.mafiacraft.core.MafiacraftCore;
import net.voxton.mafiacraft.core.config.MafiacraftConfig;
import net.voxton.mafiacraft.core.geo.MPoint;
import net.voxton.mafiacraft.core.geo.MWorld;
import net.voxton.mafiacraft.core.MafiacraftImpl;
import net.voxton.mafiacraft.core.player.MPlayer;
import org.spout.api.Spout;
import org.spout.api.geo.World;
import org.spout.api.player.Player;
import org.spout.api.plugin.CommonPlugin;

/**
 * Spout implementation of Mafiacraft.
 */
public class SpoutImpl extends CommonPlugin implements MafiacraftImpl {

    private MafiacraftCore mc;

    @Override
    public void onEnable() {
        mc = new MafiacraftCore(this);
        mc.onEnable();
    }

    @Override
    public void onDisable() {
        mc.onDisable();
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancelTasks() {
        Spout.getGame().getScheduler().cancelAllTasks();
    }

    @Override
    public void setupCommands() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerEvents() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MafiacraftConfig getMafiacraftConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MPoint getPoint(MPlayer player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleRepeatingTask(Runnable runnable, long interval) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int scheduleDelayedTask(Runnable runnable, long interval) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancelTask(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void teleportPlayer(MPlayer player, MPoint point) {
//        getPlayer(player).getEntity().setPosition(null);
    }
    
//    private 
//    
//    private Point getPoint(MPoint mpoint) {
//        return new Point()
//    }

    @Override
    public boolean isOnline(MPlayer player) {
        return getPlayer(player).isOnline();
    }

    @Override
    public void sendMessage(MPlayer player, String message) {
        getPlayer(player).sendMessage(message);
    }

    @Override
    public boolean hasPermission(MPlayer player, String permission) {
        return getPlayer(player).hasPermission(permission);
    }
    
    private Player getPlayer(MPlayer player) {
        return Spout.getGame().getPlayer(player.getName(), true);
    }

    @Override
    public Set<MPlayer> getOnlinePlayers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String matchPlayerName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MWorld getWorld(String name) {
        World world = Spout.getGame().getWorld(name);
        if (world == null) {
            return null;
        }
        return new MWorld(world.getName());
    }

    @Override
    public String getFullName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setupEconomy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getMoney(MPlayer player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double setMoney(MPlayer player, double amt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
