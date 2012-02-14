/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.player.MPlayer;

/**
 * Handles chat.
 */
public class ChatHandler {

    private MafiacraftPlugin mc;

    public ChatHandler(MafiacraftPlugin mc) {
        this.mc = mc;
    }

    /**
     * Takes in a MPlayer, and a Message, and sends the @param message to all the players in that @param player's government.
     * @param player
     * @param message 
     */
    public void handleMessage(MPlayer player, String message) {
        player.getChatType().chat(player, message);
    }
}
