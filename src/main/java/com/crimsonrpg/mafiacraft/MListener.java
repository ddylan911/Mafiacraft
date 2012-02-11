/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.geo.DistrictType;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.gov.LandOwner;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import com.crimsonrpg.mafiacraft.player.SessionStore;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author simplyianm
 */
public class MListener implements Listener {
    private final Mafiacraft mc;

    public MListener(Mafiacraft mc) {
        this.mc = mc;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        MPlayer player = mc.getPlayerManager().getPlayer(event.getPlayer());
        Chunk c = player.getPlayer().getLocation().getChunk();
        District d = mc.getCityManager().getDistrict(c);

        if (!d.getType().isBuild()) {
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

        MPlayer player = mc.getPlayerManager().getPlayer(event.getPlayer());
        Chunk c = player.getPlayer().getLocation().getChunk();
        District d = mc.getCityManager().getDistrict(c);

        if (!d.getType().isBuild()) {
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

        MPlayer player = mc.getPlayerManager().getPlayer((Player) e.getEntity());
        MPlayer damager = mc.getPlayerManager().getPlayer((Player) e.getDamager());

        District d = mc.getCityManager().getDistrict(player.getPlayer().getLocation().getChunk());

        //Check for PvP
        if (!d.getType().isPvp()) {
            damager.getPlayer().sendMessage(MsgColor.ERROR + "You aren't allowed to PvP in this district.");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }

        MPlayer player = mc.getPlayerManager().getPlayer(event.getPlayer());
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
        if (dest.getType().equals(DistrictType.RESERVED)) {
            player.getPlayer().sendMessage(MsgColor.ERROR + "You aren't allowed to enter District " + dest.getName() + ".");
            event.setCancelled(true);
            return;
        }

        //We've switched chunks!
        store.setData("lastchunk", current);
    }

}
