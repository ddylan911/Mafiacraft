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

import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.classes.UtilityClass;
import net.voxton.mafiacraft.core.city.District;
import net.voxton.mafiacraft.core.city.LandOwner;
import net.voxton.mafiacraft.core.geo.Section;
import net.voxton.mafiacraft.bukkit.BukkitImpl;
import net.voxton.mafiacraft.core.player.KillTracker;
import net.voxton.mafiacraft.core.player.MPlayer;
import net.voxton.mafiacraft.core.chat.MsgColor;
import net.voxton.mafiacraft.core.player.SessionStore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

/**
 * Mafiacraft listener.
 */
public class BukkitListener implements Listener {

    private final BukkitImpl impl;

    public BukkitListener(BukkitImpl impl) {
        this.impl = impl;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        MPlayer player = Mafiacraft.getPlayer(event.getPlayer().getName());
        Section c = impl.getPoint(event.getBlock().getLocation()).getSection();
        District d = c.getDistrict();

        if (!d.canBuild(player, c)) {
            player.sendMessage(MsgColor.ERROR
                    + "You aren't allowed to break blocks here.");
            event.setCancelled(true);
            return;
        }

        LandOwner owner = d.getOwner(c);
        if (!owner.canBuild(player, c)) {
            player.sendMessage(MsgColor.ERROR
                    + "You aren't allowed to break blocks in here; this land is owned by "
                    + owner.getOwnerName() + ".");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        MPlayer player = Mafiacraft.getPlayer(event.getPlayer().getName());
        Section c = player.getSection();
        District d = c.getDistrict();

        if (!d.canBuild(player, c)) {
            player.sendMessage(MsgColor.ERROR
                    + "You aren't allowed to place blocks here.");
            event.setCancelled(true);
            return;
        }

        LandOwner owner = d.getOwner(c);
        if (!owner.canBuild(player, c)) {
            player.sendMessage(MsgColor.ERROR
                    + "You aren't allowed to place blocks in here; this land is owned by "
                    + owner.getOwnerName() + ".");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return;
        }

        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
        if (!(e.getEntity() instanceof Player
                && e.getDamager() instanceof Player)) {
            return;
        }

        MPlayer player =
                Mafiacraft.getPlayer(((Player) e.getEntity()).getName());
        MPlayer damager =
                Mafiacraft.getPlayer(((Player) e.getDamager()).getName());

        District d = player.getDistrict();

        //Check for PvP
        if (!d.getType().isPvp()) {
            damager.sendMessage(MsgColor.ERROR
                    + "You aren't allowed to PvP in this district.");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        EntityDamageEvent cause = event.getEntity().getLastDamageCause();

        if (cause == null) {
            return;
        }

        if (!cause.getCause().equals(DamageCause.ENTITY_ATTACK)) {
            return;
        }

        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) cause;

        Entity damager = e.getDamager();
        Entity entity = e.getEntity();

        if ((!(damager instanceof Player)) || !(entity instanceof Player)) {
            return;
        }
        MPlayer attacker = Mafiacraft.getPlayer(((Player) damager).getName());
        MPlayer attacked = Mafiacraft.getPlayer(((Player) entity).getName());

        //Check for thief
        double money = attacked.getMoney() * ((attacker.getUtilityClass().equals(
                UtilityClass.THIEF)) ? 0.5 : 0.1);

        //Subtract money
        attacked.subtractMoney(money);
        attacker.addMoney(money);
        attacker.sendMessage(ChatColor.GREEN + "You killed "
                + attacked.getName() + " and took " + money + " of their money.");
        attacked.sendMessage(ChatColor.RED + "You died and lost " + money
                + " of your money.");

        //Update power
        attacker.tryToAddPower(1);
        attacked.tryToSubtractPower(attacked.getPosition().getKillCost());

        //Track the kill
        KillTracker kt = Mafiacraft.getPlayerManager().getKillTracker();
        kt.incScore(attacker);
        if (kt.getKillScore(attacked) <= 0) {
            return;
        }
        kt.decScore(attacked);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(PlayerChatEvent event) {
        MPlayer player = Mafiacraft.getPlayer((event.getPlayer()).getName());
        Mafiacraft.getChatHandler().handleMessage(player, event.getMessage());
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Mafiacraft.getChatHandler().updateDisplayName(Mafiacraft.getPlayer((event.
                getPlayer()).getName()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        MPlayer player = Mafiacraft.getPlayer((event.getPlayer()).getName());
        SessionStore store = player.getSessionStore();

        Section last = store.getObject("lastsect", Section.class);
        Section current = player.getSection();

        if (last == null) {
            store.setData("lastsect", player.getSection());
            return;
        }

        if (last.equals(current)) {
            return;
        }

        LandOwner lastOwner = last.getOwner();
        LandOwner currentOwner = current.getOwner();

        if (!lastOwner.equals(currentOwner)) {
            player.sendMessage(currentOwner.getEntryMessage());
        }

        //Check for reserved district
        District dest = current.getDistrict();
        District prev = last.getDistrict();

        if (!dest.getType().canEnter(player)) {
            long now = System.currentTimeMillis();

            long lastMoveNag = player.getSessionStore().getLong("lastmovenag",
                    now);

            if (lastMoveNag + 1000L < now) {
                player.sendMessage(MsgColor.ERROR
                        + "You aren't allowed to enter "
                        + dest.getNameInChat() + ".");
                player.getSessionStore().setData("lastmovenag", now);
            }

            //Move back
            Vector vec = new Vector(current.getX() - last.getX(), 0.0, current.
                    getZ() - last.getZ());

            event.setTo(event.getFrom().subtract(vec.normalize()));
            event.setCancelled(true);
            return;
        }

        if (prev != dest) {
            player.sendMessage(ChatColor.GRAY + "You are now entering " + dest.
                    getNameInChat() + ".");
        }

        //We've switched chunks!
        store.setData("lastchunk", current);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Mafiacraft.getPlayer((event.getPlayer()).getName()).clearSessionStore();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Mafiacraft.getPlayer((event.getPlayer()).getName()).clearSessionStore();
    }

}
