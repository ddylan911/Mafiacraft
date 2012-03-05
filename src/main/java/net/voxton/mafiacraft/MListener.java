/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft;

import net.voxton.mafiacraft.classes.UtilityClass;
import net.voxton.mafiacraft.geo.CityWorld;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.geo.LandOwner;
import net.voxton.mafiacraft.player.KillTracker;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.player.SessionStore;
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
import org.bukkit.event.player.PlayerJoinEvent;
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

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        MPlayer player = Mafiacraft.getPlayer(event.getPlayer());
        Chunk c = event.getBlock().getLocation().getChunk();
        District d = mc.getCityManager().getDistrict(c);

        if (!d.canBuild(player, c)) {
            player.sendMessage(MsgColor.ERROR + "You aren't allowed to break blocks here.");
            event.setCancelled(true);
            return;
        }

        LandOwner owner = d.getOwner(c);
        if (!owner.canBuild(player, c)) {
            player.sendMessage(MsgColor.ERROR + "You aren't allowed to break blocks in here; this land is owned by " + owner.getOwnerName() + ".");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        MPlayer player = Mafiacraft.getPlayer(event.getPlayer());
        Chunk c = player.getBukkitEntity().getLocation().getChunk();
        District d = mc.getCityManager().getDistrict(c);

        if (!d.canBuild(player, c)) {
            player.getBukkitEntity().sendMessage(MsgColor.ERROR + "You aren't allowed to place blocks here.");
            event.setCancelled(true);
            return;
        }

        LandOwner owner = d.getOwner(c);
        if (!owner.canBuild(player, c)) {
            player.getBukkitEntity().sendMessage(MsgColor.ERROR + "You aren't allowed to place blocks in here; this land is owned by " + owner.getOwnerName() + ".");
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
        if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) {
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) e.getEntity());
        MPlayer damager = Mafiacraft.getPlayer((Player) e.getDamager());

        District d = mc.getCityManager().getDistrict(player.getBukkitEntity().getLocation().getChunk());

        //Check for PvP
        if (!d.getType().isPvp()) {
            damager.getBukkitEntity().sendMessage(MsgColor.ERROR + "You aren't allowed to PvP in this district.");
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
        MPlayer attacker = Mafiacraft.getPlayer((Player) damager);
        MPlayer attacked = Mafiacraft.getPlayer((Player) entity);

        //Check for thief
        double money = attacked.getMoney() * ((attacker.getUtilityClass().equals(UtilityClass.THIEF)) ? 0.5 : 0.1);

        //Subtract money
        attacked.subtractMoney(money);
        attacker.addMoney(money);
        attacker.sendMessage(ChatColor.GREEN + "You killed " + attacked.getName() + " and took " + money + " of their money.");
        attacked.sendMessage(ChatColor.RED + "You died and lost " + money + " of your money.");

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
        MPlayer player = Mafiacraft.getPlayer(event.getPlayer());
        mc.getChatHandler().handleMessage(player, event.getMessage());
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        mc.getChatHandler().updateDisplayName(Mafiacraft.getPlayer(event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        MPlayer player = Mafiacraft.getPlayer(event.getPlayer());
        SessionStore store = player.getSessionStore();

        Chunk last = store.getObject("lastchunk", Chunk.class);
        Chunk current = player.getBukkitEntity().getLocation().getChunk();

        if (last == null) {
            store.setData("lastchunk", player.getBukkitEntity().getLocation().getChunk());
            return;
        }

        if (last.equals(current)) {
            return;
        }

        LandOwner lastOwner = Mafiacraft.getOwner(last);
        LandOwner currentOwner = Mafiacraft.getOwner(current);

        if (!lastOwner.equals(currentOwner)) {
            player.sendMessage(currentOwner.getEntryMessage());
        }

        //Check for reserved district
        District dest = mc.getCityManager().getDistrict(current);
        District prev = mc.getCityManager().getDistrict(last);

        if (!dest.getType().canEnter(player)) {
            player.sendMessage(MsgColor.ERROR + "You aren't allowed to enter " + dest.getNameInChat() + ".");

            //Move back
            Vector vec = new Vector(current.getX() - last.getX(), 0.0, current.getZ() - last.getZ());

            event.setTo(event.getFrom().subtract(vec.normalize()));
            event.setCancelled(true);
            return;
        }

        if (prev != dest) {
            player.sendMessage(ChatColor.GRAY + "You are now entering " + dest.getNameInChat() + ".");
        }

        //We've switched chunks!
        store.setData("lastchunk", current);
    }

}
