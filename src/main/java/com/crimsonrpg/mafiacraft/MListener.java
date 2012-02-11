/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

import com.crimsonrpg.mafiacraft.geo.District;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import com.crimsonrpg.mafiacraft.player.SessionStore;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        
        District d = mc.getDistrictManager().getDistrict(player.getPlayer().getLocation().getChunk());
        
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

        Player player = event.getPlayer();
        SessionStore store = Mafiacraft.getInstance().getPlayerManager().getPlayer(player).getSessionStore();

        Chunk last = store.getObject("lastchunk", Chunk.class);
        Chunk current = player.getLocation().getChunk();

        if (last == null) {
            store.setData("lastchunk", player.getLocation().getChunk());
            return;
        }

        if (last.equals(current)) {
            return;
        }

        //We've switched chunks!
        store.setData("lastchunk", current);

        //TODO: what do we do.
    }

}
