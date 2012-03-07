/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.chat;

import net.voxton.mafiacraft.MafiacraftPlugin;
import net.voxton.mafiacraft.gov.GovType;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.gov.Position;
import net.voxton.mafiacraft.player.MPlayer;
import org.bukkit.entity.Player;

/**
 * Handles chat.
 */
public class ChatHandler {

    private MafiacraftPlugin mc;

    public ChatHandler(MafiacraftPlugin mc) {
        this.mc = mc;
    }

    /**
     * Takes in a MPlayer, and a Message, and sends the
     *
     * @param message to all the players in that
     * @param player's government.
     * @param player
     * @param message
     */
    public void handleMessage(MPlayer player, String message) {
        updateDisplayName(player);
        player.getChatType().chat(player, message);
    }

    /**
     * Updates a player's display name.
     *
     * @param player
     */
    public void updateDisplayName(MPlayer player) {
        Player ent = player.getBukkitEntity();
        if (ent == null) {
            throw new IllegalStateException(
                    "Player not online to update display name of!");
        }

        String display = player.getName();

        //TODO: Do donator colors.

        //Do don title if exists.
        Government gov = player.getGovernment();
        if (gov != null && player.getPosition().equals(Position.LEADER)) {
            GovType type = gov.getType();
            if (type.equals(GovType.MAFIA)) {
                display = "Don " + display;
            } else if (type.equals(GovType.POLICE)) {
                display = "Chief " + display;
            }
        }
    }

}
