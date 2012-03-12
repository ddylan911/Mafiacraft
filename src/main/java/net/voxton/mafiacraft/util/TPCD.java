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
package net.voxton.mafiacraft.util;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Crappy teleportation system. Please replace me with some sort of factory thing!
 * 
 * TODO: FIXME!!!! HIGH PRIORITY!
 */
public class TPCD implements Runnable {

    private static TObjectIntMap<Player> teleporting =
            new TObjectIntHashMap<Player>();

    private int iterations;

    private Type type;

    private Player player;

    private Location location;

    public TPCD(int iterations, Type type, Player player, Location location) {
        this.iterations = iterations;
        this.type = type;
        this.player = player;
        this.location = location;
    }

    public void run() {
        if (iterations <= 0) {
            player.sendMessage(ChatColor.YELLOW + "Returning to "
                    + type.getName() + "...");
            player.teleport(location);
            removeCountdown(player);
        } else {
            player.sendMessage(ChatColor.YELLOW.toString() + "Teleporting in "
                    + iterations + " seconds.");
        }

        --iterations;
    }

    public static void makeCountdown(int iterations, Type type,
            Player player, Location location) {
        if (isSpawning(player)) {
            removeCountdown(player);
        }
        teleporting.put(player,
                Bukkit.getScheduler().scheduleSyncRepeatingTask(cp,
                new TPCD(iterations, type, player, location),
                0L, 20L));
    }

    public static boolean isSpawning(Player player) {
        return teleporting.containsKey(player);
    }

    public static void removeCountdown(Player player) {
        Bukkit.getScheduler().cancelTask(teleporting.get(player));
        teleporting.remove(player);
    }

    public enum Type {

        FHOME("your faction home"),
        CSPAWN("the city spawn"),
        SPAWN("spawn"),
        DBUS("the district's bus stop"),
        GOVHQ("your government's HQ");

        private String name;

        private Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}
