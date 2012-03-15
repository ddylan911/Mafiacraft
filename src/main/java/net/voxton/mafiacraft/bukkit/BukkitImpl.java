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
package net.voxton.mafiacraft.bukkit;

import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import net.voxton.mafiacraft.core.config.MafiacraftConfig;
import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.MafiacraftCore;
import net.voxton.mafiacraft.core.action.ActionPerformer;
import net.voxton.mafiacraft.core.action.ActionType;
import net.voxton.mafiacraft.core.action.Actions;
import net.voxton.mafiacraft.core.geo.MPoint;
import net.voxton.mafiacraft.core.geo.MWorld;
import net.voxton.mafiacraft.core.MafiacraftImpl;
import net.voxton.mafiacraft.core.util.logging.MLogger;
import net.voxton.mafiacraft.core.player.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Bukkit implementation of Mafiacraft.
 */
public class BukkitImpl extends JavaPlugin implements MafiacraftImpl {

    private MafiacraftCore mc;

    /**
     * Vault hook for economy.
     */
    private Economy economy = null;

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
    public String getFullName() {
        return "Bukkit " + Bukkit.getBukkitVersion();
    }

    @Override
    public void setupCommands() {

        getCommand("mafia").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.MAFIA);
                return true;
            }

        });
        getCommand("police").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.POLICE);
                return true;
            }

        });
        getCommand("city").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.CITY);
                return true;
            }

        });
        getCommand("district").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.DISTRICT);
                return true;
            }

        });
        getCommand("section").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.SECTION);
                return true;
            }

        });
        getCommand("chat").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.CHAT);
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
                BukkitImpl.this.performActionCommand(cs, cmnd, string, strings,
                        ActionType.SQUAD);
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
        List<String> largs = new ArrayList<String>(Arrays.asList(args));
        if (largs.size() > 0) {
            largs.remove(0);
        }

        actions.parseActionCommand(performer, action, largs);
    }

    @Override
    public void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().
                getServicesManager().getRegistration(
                net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        if (economy == null) {
            MLogger.log(Level.SEVERE,
                    "No supported economy by Vault detected! Things WILL go wrong!");
        }
    }

    @Override
    public void cancelTasks() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public void registerEvents() {
        BukkitListener l = new BukkitListener(this);
        Bukkit.getPluginManager().registerEvents(l, this);
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
    public Set<MPlayer> getOnlinePlayers() {
        Set<MPlayer> players = new HashSet<MPlayer>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(Mafiacraft.getPlayer(player.getName()));
        }
        return players;
    }

    @Override
    public String matchPlayerName(String name) {
        Player player = Bukkit.getPlayer(name);
        return (player == null) ? name : player.getName();
    }

    @Override
    public MWorld getWorld(String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            return null;
        }
        return new MWorld(world.getName());
    }

    @Override
    public double getMoney(MPlayer player) {
        return economy.getBalance(player.getName());
    }

    @Override
    public double setMoney(MPlayer player, double amt) {
        double deposit = amt - getMoney(player);
        if (deposit > 0) {
            return economy.depositPlayer(player.getName(), deposit).balance;
        } else if (deposit < 0) {
            return economy.withdrawPlayer(player.getName(), -deposit).balance;
        }
        return 0;
    }

    @Override
    public InputStream getJarResource(String path) {
        return getResource(path);
    }

    @Override
    public String getServerVersion() {
        return Bukkit.getVersion();
    }

}
