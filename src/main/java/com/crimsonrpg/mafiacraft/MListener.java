/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

import com.crimsonrpg.mafiacraft.classes.ClassType;
import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.geo.DistrictType;
import com.crimsonrpg.mafiacraft.gov.LandOwner;
import com.crimsonrpg.mafiacraft.player.KillTracker;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import com.crimsonrpg.mafiacraft.player.SessionStore;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
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
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 *
 * @author simplyianm
 */
public class MListener implements Listener {

    private final MafiacraftPlugin mc;

    public MListener(MafiacraftPlugin mc) {
        this.mc = mc;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        MPlayer player = Mafiacraft.getPlayer(event.getPlayer());
        Chunk c = player.getPlayer().getLocation().getChunk();
        District d = mc.getCityManager().getDistrict(c);

        if (!d.getType().canBuildAnywhere()) {
            player.getPlayer().sendMessage(MsgColor.ERROR + "You aren't allowed to break blocks here.");
            event.setCancelled(true);
            return;
        }

        LandOwner owner = d.getOwner(c);
        if (!owner.canBuild(player, c)) {
            player.getPlayer().sendMessage(MsgColor.ERROR + "You aren't allowed to break blocks in here; this land is owned by " + owner.getOwnerName() + ".");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        MPlayer player = Mafiacraft.getPlayer(event.getPlayer());
        Chunk c = player.getPlayer().getLocation().getChunk();
        District d = mc.getCityManager().getDistrict(c);

        if (!d.getType().canBuildAnywhere()) {
            player.getPlayer().sendMessage(MsgColor.ERROR + "You aren't allowed to place blocks here.");
            event.setCancelled(true);
            return;
        }

        LandOwner owner = d.getOwner(c);
        if (!owner.canBuild(player, c)) {
            player.getPlayer().sendMessage(MsgColor.ERROR + "You aren't allowed to place blocks in here; this land is owned by " + owner.getOwnerName() + ".");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event instanceof EntityDamageByEntityEvent)) {
            return;
        }

        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
        if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) {
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) e.getEntity());
        MPlayer damager = Mafiacraft.getPlayer((Player) e.getDamager());

        District d = mc.getCityManager().getDistrict(player.getPlayer().getLocation().getChunk());

        //Check for PvP
        if (!d.getType().isPvp()) {
            damager.getPlayer().sendMessage(MsgColor.ERROR + "You aren't allowed to PvP in this district.");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        EntityDamageEvent cause = event.getEntity().getLastDamageCause();
        
        if (!cause.getCause().equals(DamageCause.ENTITY_ATTACK)) {
            return;
        }
        
        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) cause;
        
        Entity damager = e.getDamager();
        Entity entity = e.getEntity();
        
        if ((!(damager instanceof Player)) || !(entity instanceof Player)) {
            return;
        }
        MPlayer attacker = Mafiacraft.getPlayer((Player) damager);
        MPlayer attacked = Mafiacraft.getPlayer((Player) entity);
        
        //Check for thief
        double money = attacked.getMoney() * ((attacker.getUtilityClass().equals(ClassType.THIEF)) ? 0.5 : 0.1);
        
        //Subtract money
        attacked.subtractMoney(money);
        attacker.addMoney(money);
        attacker.sendMessage(ChatColor.GREEN + "You killed " + attacked.getName() + " and took " + money + " of their money.");
        attacked.sendMessage(ChatColor.RED + "You died and lost " + money + " of your money.");
            
        //Track the kill
        KillTracker kt = Mafiacraft.getPlayerManager().getKillTracker();
        kt.incScore(attacker);
        if (kt.getKillScore(attacked) <= 0) {
            return;
        }
        kt.decScore(attacked);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        MPlayer player = Mafiacraft.getPlayer(event.getPlayer());
        mc.getChatHandler().handleMessage(player, event.getMessage());
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }

        MPlayer player = Mafiacraft.getPlayer(event.getPlayer());
        SessionStore store = player.getSessionStore();

        Chunk last = store.getObject("lastchunk", Chunk.class);
        Chunk current = player.getPlayer().getLocation().getChunk();

        if (last == null) {
            store.setData("lastchunk", player.getPlayer().getLocation().getChunk());
            return;
        }

        if (last.equals(current)) {
            return;
        }

        //Check for reserved district
        District dest = mc.getCityManager().getDistrict(current);
        District prev = mc.getCityManager().getDistrict(last);

        if (dest.getType().equals(DistrictType.RESERVED)) {
            player.getPlayer().sendMessage(MsgColor.ERROR + "You aren't allowed to enter District " + dest.getName() + ".");

            //Move back
            Vector vec = new Vector(current.getX() - last.getX(), 0.0, current.getZ() - last.getZ());

            event.setTo(event.getFrom().subtract(vec.normalize()));
            event.setCancelled(true);
            return;
        }

        if (prev != dest) {
            player.sendMessage(ChatColor.GRAY + "You are now entering " + dest.getName() + ".");
        }

        //We've switched chunks!
        store.setData("lastchunk", current);
    }
}
