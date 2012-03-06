/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author simplyianm
 */
public class TPCD implements Runnable {

    private static TObjectIntMap<Player> teleporting = new TObjectIntHashMap<Player>();

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
            player.sendMessage(ChatColor.YELLOW + "Returning to " + type.getName() + "...");
            player.teleport(location);
            removeCountdown(player);
        } else {
            player.sendMessage(ChatColor.YELLOW.toString() + "Teleporting in " + iterations + " seconds.");
        }

        --iterations;
    }

    public static void makeCountdown(JavaPlugin cp, int iterations, Type type, Player player, Location location) {
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
