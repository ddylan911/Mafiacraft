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
package net.voxton.mafiacraft.impl.bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.voxton.mafiacraft.config.MafiacraftConfig;
import net.voxton.mafiacraft.MListener;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.MafiacraftCore;
import net.voxton.mafiacraft.action.ActionPerformer;
import net.voxton.mafiacraft.action.ActionType;
import net.voxton.mafiacraft.action.Actions;
import net.voxton.mafiacraft.action.action.CWorldActions;
import net.voxton.mafiacraft.action.action.ChatActions;
import net.voxton.mafiacraft.action.action.CityActions;
import net.voxton.mafiacraft.action.action.DistrictActions;
import net.voxton.mafiacraft.action.action.DivisionActions;
import net.voxton.mafiacraft.action.action.GovernmentActions;
import net.voxton.mafiacraft.action.action.SectionActions;
import net.voxton.mafiacraft.geo.MPoint;
import net.voxton.mafiacraft.gov.GovType;
import net.voxton.mafiacraft.impl.MafiacraftImpl;
import net.voxton.mafiacraft.player.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Bukkit implementation of Mafiacraft.
 */
public class BukkitImpl extends JavaPlugin implements MafiacraftImpl {

    private MafiacraftCore mc;

    @Override
    public void onDisable() {
        mc.onDisable();
    }

    @Override
    public void onEnable() {
        mc = new MafiacraftCore(this);
        mc.onEnable();
    }

    @Override
    public void setupCommands() {

        getCommand("mafia").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                GovernmentActions.parseCmd(cs, cmnd, string, strings,
                        GovType.MAFIA);
                return true;
            }

        });
        getCommand("police").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                GovernmentActions.parseCmd(cs, cmnd, string, strings,
                        GovType.POLICE);
                return true;
            }

        });
        getCommand("city").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                CityActions.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        getCommand("district").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                DistrictActions.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        getCommand("section").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                SectionActions.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        getCommand("chat").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                ChatActions.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        getCommand("cworld").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.CWORLD);
                return true;
            }

        });
        getCommand("regime").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.REGIME);
                return true;
            }

        });
        getCommand("squad").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                DivisionActions.parseCmd(cs, cmnd, string, strings,
                        GovType.POLICE);
                return true;
            }

        });
        getCommand("mafiacraft").setExecutor(new CommandExecutor() {

            @Override
            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.MAFIACRAFT);
                return true;
            }

        });
    }

    private void performActionCommand(CommandSender sender, Command cmd,
            String label, String[] args, Actions actions) {
        ActionPerformer performer = null;
        if (sender instanceof Player) {
            performer = Mafiacraft.getPlayer(((Player) sender).getName());
        } else {
            performer = Mafiacraft.getConsolePerformer();
        }

        //Get the action we want to do.
        String action = (args.length > 0) ? args[0] : "";
        List<String> largs = Arrays.asList(args);
        largs.remove(0);

        actions.parseActionCommand(performer, action, largs);
    }

    @Override
    public void cancelTasks() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public void registerEvents() {
        MListener l = new MListener(this);
        Bukkit.getPluginManager().registerEvents(l, this);
    }

    /**
     * Gets the version of the plugin.
     *
     * @return The version of the plugin.
     */
    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    @Override
    public MafiacraftConfig getMafiacraftConfig() {
        return new BukkitConfig(this);
    }

    @Override
    public MPoint getPoint(MPlayer player) {
        Location location = Bukkit.getPlayer(player.getName()).getLocation();
        return getPoint(location);
    }

    /**
     * Gets an MPoint from a location.
     * 
     * @param location
     * @return 
     */
    public MPoint getPoint(Location location) {
        return new MPoint(Mafiacraft.getWorld(location.getWorld().getName()),
                location.getX(), location.getY(), location.getZ());
    }

    @Override
    public int scheduleRepeatingTask(Runnable runnable, long interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(this, runnable,
                interval, interval);
    }

    @Override
    public int scheduleDelayedTask(Runnable runnable, long interval) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(this, runnable,
                interval);
    }

    @Override
    public void cancelTask(int id) {
        Bukkit.getScheduler().cancelTask(id);
    }

    @Override
    public void teleportPlayer(MPlayer player, MPoint point) {
        Player bk = getOfflinePlayer(player).getPlayer();
        if (bk == null) {
            return;
        }
        World world = Bukkit.getWorld(point.getWorld().getName());
        Location location = new Location(world, point.getX(), point.getY(),
                point.getZ());
        bk.teleport(location);
    }

    @Override
    public boolean isOnline(MPlayer player) {
        return getOfflinePlayer(player).isOnline();
    }

    private OfflinePlayer getOfflinePlayer(MPlayer mplayer) {
        return Bukkit.getOfflinePlayer(mplayer.getName());
    }

    @Override
    public void sendMessage(MPlayer player, String message) {
        OfflinePlayer op = getOfflinePlayer(player);
        if (op == null) {
            return;
        }
        op.getPlayer().sendMessage(message);
    }

    @Override
    public boolean hasPermission(MPlayer player, String permission) {
        OfflinePlayer op = getOfflinePlayer(player);
        if (!op.isOnline()) {
            throw new IllegalArgumentException(
                    "Player must be online to check permissions!");
        }
        return op.getPlayer().hasPermission(permission);
    }

    @Override
    public List<MPlayer> getOnlinePlayers() {
        List<MPlayer> players = new ArrayList<MPlayer>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(Mafiacraft.getPlayer(player.getName()));
        }
        return players;
    }

    @Override
    public String matchName(String name) {
        Player player = Bukkit.getPlayer(name);
        return (player == null) ? name : player.getName();
    }

}
